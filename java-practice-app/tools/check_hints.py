"""Runs real javac on broken snippets and checks that app/hints.py explains each one.

Usage (from java-practice-app/):  python tools/check_hints.py
Needs javac on PATH. Exits non-zero if any snippet gets no explanation."""
import os
import subprocess
import sys
import tempfile

sys.path.insert(0, os.path.join(os.path.dirname(os.path.abspath(__file__)), ".."))
from app import hints  # noqa: E402

SNIPPETS = {
    "missing semicolon": 'public class Main { public static void main(String[] a){ int x = 5 System.out.println(x); } }',
    "cannot find symbol": 'public class Main { public static void main(String[] a){ System.out.println(y); } }',
    "lossy conversion": 'public class Main { public static void main(String[] a){ int x = 3.5; } }',
    "type mismatch": 'public class Main { public static void main(String[] a){ String s = 5; } }',
    "duplicate variable": 'public class Main { public static void main(String[] a){ int a1 = 1; int a1 = 2; } }',
    "uninitialized": 'public class Main { public static void main(String[] a){ int q; System.out.println(q); } }',
    "missing return": 'public class Main { static int f(){ } public static void main(String[] a){ } }',
    "missing brace": 'public class Main { public static void main(String[] a){ System.out.println("x"); }',
    "static context": 'public class Main { void g(){} public static void main(String[] a){ g(); } }',
    "unreported exception": 'public class Main { public static void main(String[] a){ Thread.sleep(5); } }',
    "unclosed string": 'public class Main { public static void main(String[] a){ String s = "abc; } }',
    "wrong class name": 'public class Foo { public static void main(String[] a){ } }',
    "wrong arguments": 'public class Main { static void f(int a){} public static void main(String[] a){ f(1, 2); } }',
    "unreachable": 'public class Main { static void f(){ return; System.out.println(); } public static void main(String[] a){ } }',
}


def main() -> int:
    failed = 0
    for name, src in SNIPPETS.items():
        with tempfile.TemporaryDirectory() as d:
            with open(os.path.join(d, "Main.java"), "w", encoding="utf-8") as f:
                f.write(src)
            out = subprocess.run(["javac", "Main.java"], cwd=d, capture_output=True, text=True).stderr
        if hints.explain(out):
            print("ok   ", name)
        else:
            failed += 1
            print("MISS ", name, "\n", out)
    print(f"{len(SNIPPETS) - failed}/{len(SNIPPETS)} explained")
    return 1 if failed else 0


if __name__ == "__main__":
    sys.exit(main())
