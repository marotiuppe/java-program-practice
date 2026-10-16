"""Extra isolation for learner programs (Linux only): a seccomp-BPF syscall filter.

What it does, in the child process just before it runs `javac` / `java`:
  * socket(): only AF_UNIX is allowed, so user code cannot open TCP/UDP/raw sockets (no network access).
  * a list of dangerous syscalls (ptrace, mount, unshare, bpf, io_uring, ...) fail with EPERM.
  * any other architecture (e.g. 32-bit compat syscalls) is refused, which closes the usual filter bypass.
Needs no extra privileges (uses PR_SET_NO_NEW_PRIVS). It is NOT a full container: the program can still read
world-readable files and write to /tmp as the unprivileged runner user.

Because a bad filter would break every run, the app only enables it after `runner.sandbox_self_test()`
proves a real Java program still runs and that a network connection is really refused. Set SANDBOX=off to skip."""
import ctypes
import ctypes.util
import os
import platform
import struct

ENABLED = False
STATUS = "not checked"  # shown on the admin page

AF_UNIX = 1
EPERM = 1
SECCOMP_RET_ALLOW = 0x7FFF0000
SECCOMP_RET_ERRNO = 0x00050000
PR_SET_NO_NEW_PRIVS = 38
PR_SET_SECCOMP = 22
SECCOMP_MODE_FILTER = 2

# classic BPF opcodes
BPF_LD_W_ABS = 0x20
BPF_JEQ_K = 0x15
BPF_JGE_K = 0x35
BPF_RET_K = 0x06

AUDIT_ARCH = {"x86_64": 0xC000003E, "aarch64": 0xC00000B7}
X32_FLAG = 0x40000000

# name -> syscall number, per architecture
SYSCALLS = {
    "x86_64": {"socket": 41, "ptrace": 101, "mount": 165, "umount2": 166, "unshare": 272, "setns": 308, "bpf": 321,
               "perf_event_open": 298, "keyctl": 250, "add_key": 248, "request_key": 249, "process_vm_readv": 310,
               "process_vm_writev": 311, "kexec_load": 246, "init_module": 175, "finit_module": 313,
               "delete_module": 176, "reboot": 169, "swapon": 167, "pivot_root": 155, "chroot": 161,
               "io_uring_setup": 425, "userfaultfd": 323, "open_by_handle_at": 304},
    "aarch64": {"socket": 198, "ptrace": 117, "mount": 40, "umount2": 39, "unshare": 97, "setns": 268, "bpf": 280,
                "perf_event_open": 241, "keyctl": 219, "add_key": 217, "request_key": 218, "process_vm_readv": 270,
                "process_vm_writev": 271, "kexec_load": 104, "init_module": 105, "finit_module": 273,
                "delete_module": 106, "reboot": 142, "swapon": 224, "pivot_root": 41, "chroot": 51,
                "io_uring_setup": 425, "userfaultfd": 282, "open_by_handle_at": 265},
}


def supported() -> bool:
    return os.name == "posix" and platform.system() == "Linux" and platform.machine() in AUDIT_ARCH


def build_filter(machine: str) -> bytes:
    """The BPF program as bytes (an array of struct sock_filter)."""
    nums = SYSCALLS[machine]
    blocked = [n for name, n in nums.items() if name != "socket"]
    # Layout (indices are filled in once the length is known):
    #   0 load arch ; 1 arch ok? ; 2 load nr ; [3 x32 check, x86_64 only]
    #   one JEQ per blocked syscall ; JEQ socket ; ALLOW ; load arg0 ; JEQ AF_UNIX ; DENY
    ins = []  # (code, jt, jf, k) with jt/jf as label names or ints, resolved below
    labels = {}

    def emit(code, k, jt=0, jf=0):
        ins.append([code, jt, jf, k])

    emit(BPF_LD_W_ABS, 4)                                   # arch
    emit(BPF_JEQ_K, AUDIT_ARCH[machine], 0, "deny")         # wrong arch -> deny
    emit(BPF_LD_W_ABS, 0)                                   # syscall number
    if machine == "x86_64":
        emit(BPF_JGE_K, X32_FLAG, "deny", 0)                # x32 ABI numbers -> deny
    for n in blocked:
        emit(BPF_JEQ_K, n, "deny", 0)
    emit(BPF_JEQ_K, nums["socket"], "sockchk", 0)
    emit(BPF_RET_K, SECCOMP_RET_ALLOW)
    labels["sockchk"] = len(ins)
    emit(BPF_LD_W_ABS, 16)                                  # args[0] (domain), low 32 bits
    emit(BPF_JEQ_K, AF_UNIX, "allow", "deny")
    labels["allow"] = len(ins)
    emit(BPF_RET_K, SECCOMP_RET_ALLOW)
    labels["deny"] = len(ins)
    emit(BPF_RET_K, SECCOMP_RET_ERRNO | EPERM)

    out = b""
    for i, (code, jt, jf, k) in enumerate(ins):
        jt = labels[jt] - i - 1 if isinstance(jt, str) else jt
        jf = labels[jf] - i - 1 if isinstance(jf, str) else jf
        assert 0 <= jt < 256 and 0 <= jf < 256, "BPF jump out of range"
        out += struct.pack("HBBI", code, jt, jf, k)
    return out


_program = None  # (buffer, fprog) kept alive for the lifetime of the process


def install() -> None:
    """Apply the filter to the current process (call in the child, right before exec). Irreversible."""
    global _program
    if _program is None:
        raw = build_filter(platform.machine())
        buf = ctypes.create_string_buffer(raw, len(raw))
        fprog = struct.pack("HxxxxxxP", len(raw) // 8, ctypes.addressof(buf))
        _program = (buf, ctypes.create_string_buffer(fprog, len(fprog)))
    libc = ctypes.CDLL(ctypes.util.find_library("c") or None, use_errno=True)
    # prctl is variadic; without explicit types ctypes would pass the buffer address as a 32-bit int.
    libc.prctl.argtypes = [ctypes.c_int, ctypes.c_ulong, ctypes.c_ulong, ctypes.c_ulong, ctypes.c_ulong]
    libc.prctl.restype = ctypes.c_int
    if libc.prctl(PR_SET_NO_NEW_PRIVS, 1, 0, 0, 0) != 0:
        raise OSError(ctypes.get_errno(), "PR_SET_NO_NEW_PRIVS failed")
    if libc.prctl(PR_SET_SECCOMP, SECCOMP_MODE_FILTER, ctypes.addressof(_program[1]), 0, 0) != 0:
        raise OSError(ctypes.get_errno(), "PR_SET_SECCOMP failed")
