"""Compiles and runs a single-file Java program (class Main) with limits.

Safety layers: fresh temp dir, wall-clock timeouts, resource limits, scrubbed
environment, output captured to size-capped files, (when started as root in the
container) the child drops to an unprivileged uid, and on Linux a seccomp filter
(app/sandbox.py) blocks network sockets once the startup self-test has passed.
"""
import asyncio
import hashlib
import logging
import os
import shutil
import signal
import subprocess
import socket
import tempfile
import threading

from . import sandbox

log = logging.getLogger("runner")
JAVA_BIN_DIR = os.environ.get("JAVA_BIN_DIR", "")  # empty -> use PATH
_JAVAC_BASE = "-J-Xmx192m -J-XX:+UseSerialGC -J-XX:TieredStopAtLevel=1 -encoding UTF-8 -proc:none"
_javac_flags_cache = None
JAVA_FLAGS = os.environ.get(
    "JAVA_FLAGS", "-Xmx96m -Xss1m -XX:+UseSerialGC -XX:TieredStopAtLevel=1 -Xshare:auto"
).split()

COMPILE_TIMEOUT = int(os.environ.get("COMPILE_TIMEOUT", "30"))
RUN_TIMEOUT = int(os.environ.get("RUN_TIMEOUT", "10"))
MAX_CODE_BYTES = 50_000
MAX_STDIN_BYTES = 20_000
MAX_OUTPUT_BYTES = 20_000
RUNNER_UID = int(os.environ.get("RUNNER_UID", "10001"))

# One Java process at a time: the free plan has ~512 MB of RAM.
_slot = asyncio.Semaphore(1)
MAX_QUEUE = int(os.environ.get("RUN_QUEUE_MAX", "6"))  # programs allowed to wait for the slot
_waiting = 0
sandbox_ready = threading.Event()  # set once the sandbox self-test has finished (pass or fail)
sandbox_check_started = threading.Event()  # set by the web app at startup; scripts that skip it never wait


def queue_depth() -> int:
    """How many programs are waiting for the single Java slot right now."""
    return _waiting
_IS_POSIX = os.name == "posix"


def _exe(name: str) -> str:
    if JAVA_BIN_DIR:
        return os.path.join(JAVA_BIN_DIR, name)
    return name


def _javac_major() -> int:
    """Major version of the javac in use (8, 11, 17, ...), or 0 if it cannot be determined."""
    try:
        r = subprocess.run([_exe("javac"), "-version"], capture_output=True, text=True, timeout=20)
        text = (r.stdout + r.stderr).strip()  # e.g. "javac 17.0.9" or "javac 1.8.0_311"
        ver = text.split()[-1]
        parts = ver.split(".")
        return int(parts[1]) if parts[0] == "1" else int(parts[0])
    except Exception:
        return 0


def _javac_flags() -> list:
    """--release 17 only exists on javac 17+; older compilers (e.g. JDK 8) reject it."""
    global _javac_flags_cache
    if _javac_flags_cache is None:
        override = os.environ.get("JAVAC_FLAGS")
        if override:
            _javac_flags_cache = override.split()
        else:
            flags = _JAVAC_BASE.split()
            if _javac_major() >= 17:
                flags += ["--release", "17"]
            _javac_flags_cache = flags
    return _javac_flags_cache


def _preexec():
    import resource

    resource.setrlimit(resource.RLIMIT_CPU, (20, 20))
    resource.setrlimit(resource.RLIMIT_FSIZE, (2_000_000, 2_000_000))
    resource.setrlimit(resource.RLIMIT_NPROC, (256, 256))
    resource.setrlimit(resource.RLIMIT_NOFILE, (256, 256))
    resource.setrlimit(resource.RLIMIT_CORE, (0, 0))
    if os.getuid() == 0:
        os.setgroups([])
        os.setgid(RUNNER_UID)
        os.setuid(RUNNER_UID)
    if sandbox.ENABLED:
        sandbox.install()  # last step: after this the child cannot open network sockets


def _kill(proc: subprocess.Popen) -> None:
    try:
        if _IS_POSIX:
            os.killpg(proc.pid, signal.SIGKILL)
        else:
            proc.kill()
    except Exception:
        pass


def _read_capped(path: str) -> str:
    try:
        with open(path, "rb") as f:
            data = f.read(MAX_OUTPUT_BYTES + 1)
    except FileNotFoundError:
        return ""
    text = data[:MAX_OUTPUT_BYTES].decode("utf-8", errors="replace")
    if len(data) > MAX_OUTPUT_BYTES:
        text += "\n... output truncated ..."
    return text


def _run_process(cmd, cwd, env, stdin_path, stdout_path, stderr_path, timeout):
    with open(stdin_path, "rb") as fin, open(stdout_path, "wb") as fout, open(stderr_path, "wb") as ferr:
        proc = subprocess.Popen(
            cmd,
            cwd=cwd,
            env=env,
            stdin=fin,
            stdout=fout,
            stderr=ferr,
            start_new_session=_IS_POSIX,
            preexec_fn=_preexec if _IS_POSIX else None,
        )
        try:
            proc.wait(timeout=timeout)
            return proc.returncode, False
        except subprocess.TimeoutExpired:
            _kill(proc)
            proc.wait()
            return -1, True


def _prep(tmp: str, code: str):
    """Write Main.java into tmp and return (src_path, env, drop_privileges)."""
    os.chmod(tmp, 0o755)
    drop = _IS_POSIX and os.getuid() == 0
    if drop:
        os.chown(tmp, RUNNER_UID, RUNNER_UID)
    src = os.path.join(tmp, "Main.java")
    with open(src, "w", encoding="utf-8") as f:
        f.write(code)
    if drop:
        os.chown(src, RUNNER_UID, RUNNER_UID)
    env = {"PATH": os.environ.get("PATH", ""), "HOME": tmp, "LANG": "C.UTF-8", "TMPDIR": tmp, "TEMP": tmp, "TMP": tmp}
    if os.environ.get("SYSTEMROOT"):  # Windows (local development) needs this to start the JVM
        env["SYSTEMROOT"] = os.environ["SYSTEMROOT"]
    return src, env, drop


def _compile(tmp: str, src: str, env: dict):
    """Returns None on success, or a compile-failure result dict."""
    empty = os.path.join(tmp, "empty.txt")
    open(empty, "w").close()
    out_path = os.path.join(tmp, "cout.txt")
    err_path = os.path.join(tmp, "cerr.txt")
    rc, timed_out = _run_process(
        [_exe("javac"), *_javac_flags(), "-d", tmp, src], tmp, env, empty, out_path, err_path, COMPILE_TIMEOUT
    )
    if timed_out:
        return {"stage": "compile", "ok": False, "error": "Compilation timed out."}
    if rc != 0:
        msg = _read_capped(err_path) or _read_capped(out_path)
        return {"stage": "compile", "ok": False, "error": msg.replace(tmp + os.sep, "")}
    return None


CACHE_DIR = os.path.join(tempfile.gettempdir(), "jcache")
CACHE_MAX = int(os.environ.get("COMPILE_CACHE_MAX", "40"))
_cache_lock = threading.Lock()


def _cache_key(code: str) -> str:
    return hashlib.sha256((" ".join(_javac_flags()) + "\n" + code).encode("utf-8")).hexdigest()[:32]


def _cache_put(key: str, tmp: str) -> str:
    """Keep the compiled classes for this exact source so a repeat run skips javac."""
    dest = os.path.join(CACHE_DIR, key)
    try:
        with _cache_lock:
            os.makedirs(CACHE_DIR, exist_ok=True)
            os.chmod(CACHE_DIR, 0o755)
            if os.path.isdir(dest):
                return dest
            stage = dest + ".tmp"
            shutil.rmtree(stage, ignore_errors=True)
            os.makedirs(stage)
            for name in os.listdir(tmp):
                if name.endswith(".class"):
                    shutil.copy2(os.path.join(tmp, name), os.path.join(stage, name))
            os.chmod(stage, 0o755)
            os.rename(stage, dest)
            entries = sorted(
                (e for e in os.scandir(CACHE_DIR) if e.is_dir() and not e.name.endswith(".tmp")),
                key=lambda e: e.stat().st_mtime,
            )
            for old in entries[:-CACHE_MAX]:
                shutil.rmtree(old.path, ignore_errors=True)
            return dest
    except OSError:
        return tmp  # caching is best effort; fall back to the freshly compiled classes


def _build(tmp: str, src: str, env: dict, code: str):
    """Returns (class_dir, None) on success or (None, failure_result)."""
    key = _cache_key(code)
    hit = os.path.join(CACHE_DIR, key)
    if os.path.isdir(hit):
        try:
            os.utime(hit)
        except OSError:
            pass
        return hit, None
    failed = _compile(tmp, src, env)
    if failed:
        return None, failed
    return _cache_put(key, tmp), None


def _run_one(tmp: str, env: dict, drop: bool, stdin_text: str, tag: int, classdir: str) -> dict:
    in_path = os.path.join(tmp, f"stdin_{tag}.txt")
    out_path = os.path.join(tmp, f"out_{tag}.txt")
    err_path = os.path.join(tmp, f"err_{tag}.txt")
    with open(in_path, "w", encoding="utf-8") as f:
        f.write(stdin_text)
    if drop:
        os.chown(in_path, RUNNER_UID, RUNNER_UID)
    rc, timed_out = _run_process(
        [_exe("java"), *JAVA_FLAGS, f"-Djava.io.tmpdir={tmp}", "-cp", classdir, "Main"],
        tmp, env, in_path, out_path, err_path, RUN_TIMEOUT
    )
    result = {
        "ok": rc == 0 and not timed_out,
        "stdout": _read_capped(out_path),
        "stderr": _read_capped(err_path),
        "exit_code": rc,
        "timed_out": timed_out,
    }
    if timed_out:
        result["stderr"] += f"\nTime limit exceeded ({RUN_TIMEOUT}s)."
    return result


def _execute(code: str, stdin_text: str) -> dict:
    tmp = tempfile.mkdtemp(prefix="jrun_")
    try:
        src, env, drop = _prep(tmp, code)
        classdir, failed = _build(tmp, src, env, code)
        if failed:
            return failed
        result = _run_one(tmp, env, drop, stdin_text, 0, classdir)
        result["stage"] = "run"
        return result
    finally:
        shutil.rmtree(tmp, ignore_errors=True)


def _execute_batch(code: str, stdins: list) -> dict:
    """Compile once, then run the program once per stdin text."""
    tmp = tempfile.mkdtemp(prefix="jrun_")
    try:
        src, env, drop = _prep(tmp, code)
        classdir, failed = _build(tmp, src, env, code)
        if failed:
            return failed
        return {"stage": "tests", "results": [_run_one(tmp, env, drop, s, i, classdir) for i, s in enumerate(stdins)]}
    finally:
        shutil.rmtree(tmp, ignore_errors=True)


def _busy(waiting: int) -> dict:
    who = f"{waiting} other program{'' if waiting == 1 else 's'} waiting" if waiting else "other programs running"
    return {"stage": "busy", "ok": False, "queue": waiting,
            "error": f"The server is busy ({who}). Please try again in a few seconds."}


async def _guarded(fn, *args) -> dict:
    global _waiting
    # Learner code must never run before we know whether the network filter works.
    for _ in range(240):
        if sandbox_ready.is_set() or not sandbox_check_started.is_set():
            break
        await asyncio.sleep(0.5)
    else:
        return {"stage": "busy", "ok": False, "error": "The server is still starting up. Please try again in a moment."}
    if _slot.locked() and _waiting >= MAX_QUEUE:  # refuse early instead of piling up requests
        log.warning("runner queue full (%d waiting)", _waiting)
        return _busy(_waiting)
    _waiting += 1
    try:
        await asyncio.wait_for(_slot.acquire(), timeout=25)
    except asyncio.TimeoutError:
        log.warning("runner busy: gave up waiting for a free slot (fn=%s)", fn.__name__)
        return _busy(_waiting - 1)
    finally:
        _waiting -= 1
    try:
        return await asyncio.to_thread(fn, *args)
    except Exception:
        log.exception("runner failure (fn=%s)", fn.__name__)
        return {"stage": "compile", "ok": False, "error": "The server could not run your program. Please try again."}
    finally:
        _slot.release()


async def run_java(code: str, stdin_text: str) -> dict:
    if len(code.encode()) > MAX_CODE_BYTES:
        return {"stage": "compile", "ok": False, "error": "Code is too large."}
    if len(stdin_text.encode()) > MAX_STDIN_BYTES:
        return {"stage": "compile", "ok": False, "error": "Input is too large."}
    return await _guarded(_execute, code, stdin_text)


async def run_java_batch(code: str, stdins: list) -> dict:
    if len(code.encode()) > MAX_CODE_BYTES:
        return {"stage": "compile", "ok": False, "error": "Code is too large."}
    return await _guarded(_execute_batch, code, stdins)


_HELLO = 'public class Main { public static void main(String[] a){ System.out.println("sandbox-ok"); } }'
_NET_PROBE = (
    'import java.net.*;\n'
    'public class Main { public static void main(String[] a) {\n'
    '  try (Socket s = new Socket()) { s.connect(new InetSocketAddress("127.0.0.1", __PORT__), 3000);\n'
    '    System.out.println("CONNECTED"); }\n'
    '  catch (Exception e) { System.out.println("BLOCKED"); } } }'
)


def sandbox_self_test() -> str:
    """Decide whether the seccomp filter can be switched on, and set sandbox.ENABLED / sandbox.STATUS.

    Passes only if, with the filter on, (1) a normal Java program still compiles and runs and (2) a connection
    to a listening local port is refused. Blocking: call from a thread at startup."""
    try:
        return _sandbox_self_test()
    finally:
        sandbox_ready.set()


def _sandbox_self_test() -> str:
    sandbox.ENABLED = False
    if os.environ.get("SANDBOX", "").lower() == "off":
        sandbox.STATUS = "off (SANDBOX=off): network access from user code is NOT blocked"
        return sandbox.STATUS
    if not sandbox.supported():
        sandbox.STATUS = "unavailable on this platform: network access from user code is NOT blocked"
        return sandbox.STATUS
    listener = socket.socket()
    try:
        listener.bind(("127.0.0.1", 0))
        listener.listen(8)
        port = listener.getsockname()[1]
        sandbox.ENABLED = True
        hello = _execute(_HELLO, "")
        if hello.get("stage") != "run" or "sandbox-ok" not in hello.get("stdout", ""):
            raise RuntimeError(f"normal program failed under the filter: {hello}")
        probe = _execute(_NET_PROBE.replace("__PORT__", str(port)), "")
        out = probe.get("stdout", "")
        if probe.get("stage") != "run" or "BLOCKED" not in out or "CONNECTED" in out:
            raise RuntimeError(f"network probe was not blocked: {probe}")
        sandbox.STATUS = "on: user code cannot open network sockets (seccomp filter active)"
    except Exception as exc:
        sandbox.ENABLED = False
        sandbox.STATUS = f"FAILED self-test, filter disabled, network NOT blocked: {exc}"
        log.error("sandbox self-test failed: %s", exc)
    finally:
        listener.close()
    log.info("sandbox: %s", sandbox.STATUS)
    return sandbox.STATUS
