"""Checks the sandbox against real Java programs. Run it INSIDE the Docker image (Linux, JDK installed):

    docker build -t java-practice-test .
    docker run --rm java-practice-test python tools/check_sandbox.py

It proves that (1) the filter turns on, (2) the same network probe CONNECTS when the filter is off, so the
test is meaningful, (3) network and DNS are blocked for user code, and (4) ordinary programs still work."""
import os
import socket
import sys

sys.path.insert(0, os.path.join(os.path.dirname(os.path.abspath(__file__)), ".."))
from app import runner, sandbox  # noqa: E402

failures = []


def check(name, ok, detail=""):
    print(("ok    " if ok else "FAIL  ") + name + ("" if ok else f"  -> {detail}"))
    if not ok:
        failures.append(name)


def run(code, stdin=""):
    return runner._execute(code, stdin)


PROBE = runner._NET_PROBE
listener = socket.socket()
listener.bind(("127.0.0.1", 0))
listener.listen(8)
port = listener.getsockname()[1]
probe = PROBE.replace("__PORT__", str(port))

# 1. control: with the filter OFF the probe must connect
sandbox.ENABLED = False
r = run(probe)
check("control: probe connects without the filter", r.get("stdout", "").strip() == "CONNECTED", r)

# 2. the self-test turns the filter on
status = runner.sandbox_self_test()
check("self-test enables the filter", sandbox.ENABLED, status)

# 3. with the filter ON
r = run(probe)
check("probe is blocked with the filter", r.get("stdout", "").strip() == "BLOCKED", r)

dns = '''import java.net.*;
public class Main { public static void main(String[] a) {
  try { InetAddress.getByName("example.com"); System.out.println("RESOLVED"); }
  catch (Exception e) { System.out.println("NO-DNS"); } } }'''
r = run(dns)
check("DNS lookup fails", r.get("stdout", "").strip() == "NO-DNS", r)

http = '''import java.net.*;
public class Main { public static void main(String[] a) {
  try { new URL("http://93.184.216.34/").openStream(); System.out.println("FETCHED"); }
  catch (Exception e) { System.out.println("NO-HTTP"); } } }'''
r = run(http)
check("outbound HTTP fails", r.get("stdout", "").strip() == "NO-HTTP", r)

server = '''import java.net.*;
public class Main { public static void main(String[] a) {
  try (ServerSocket s = new ServerSocket(0)) { System.out.println("LISTENING"); }
  catch (Exception e) { System.out.println("NO-SERVER"); } } }'''
r = run(server)
check("cannot open a listening socket", r.get("stdout", "").strip() == "NO-SERVER", r)

# 4. ordinary programs still work
r = run('''import java.util.*;
public class Main { public static void main(String[] a) { Scanner sc = new Scanner(System.in);
  int n = sc.nextInt(); List<Integer> l = new ArrayList<>(); for (int i = 0; i < n; i++) l.add(i * i);
  System.out.println(l); } }''', "5\n")
check("Scanner + collections", r.get("stdout", "").strip() == "[0, 1, 4, 9, 16]", r)

r = run('''import java.util.concurrent.*;
public class Main { public static void main(String[] a) throws Exception {
  ExecutorService ex = Executors.newFixedThreadPool(4); Future<Integer> f = ex.submit(() -> 6 * 7);
  System.out.println(f.get()); ex.shutdown(); } }''')
check("threads / executor", r.get("stdout", "").strip() == "42", r)

r = run('''import java.io.*;
public class Main { public static void main(String[] a) throws Exception {
  File f = new File("out.txt"); try (PrintWriter w = new PrintWriter(f)) { w.println("hi"); }
  System.out.println(new BufferedReader(new FileReader(f)).readLine()); } }''')
check("file I/O in the working directory", r.get("stdout", "").strip() == "hi", r)

listener.close()
print("\nFAILED: " + ", ".join(failures) if failures else "\nAll sandbox checks passed.")
sys.exit(1 if failures else 0)
