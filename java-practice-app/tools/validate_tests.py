"""Validate question test cases against reference solutions.

Reference solutions live in tools/ref/<level>/d<DD>q<N>.java (corrected code for Debug questions).
For every reference found, this runs it against the question's visible samples AND hidden tests
from app/content/tests/<level>.json and reports any mismatch. It also lists coding questions whose
samples have no reference yet ("unverified").

Usage (from java-practice-app/):   python tools/validate_tests.py [--level beginner] [--missing]
Set JAVA_BIN_DIR to a JDK 17+ bin folder if java/javac on PATH are older.
"""
import argparse
import pathlib
import sys

ROOT = pathlib.Path(__file__).resolve().parent.parent
sys.path.insert(0, str(ROOT))

from app import content, grading, runner  # noqa: E402


def main() -> int:
    ap = argparse.ArgumentParser()
    ap.add_argument("--level", choices=list(content.LEVELS), help="only this level")
    ap.add_argument("--missing", action="store_true", help="list questions that have checks but no reference")
    args = ap.parse_args()

    content.load()
    failures = checked = unverified = 0
    for level, data in content.CONTENT.items():
        if args.level and level != args.level:
            continue
        for dn, day in sorted(data["days"].items()):
            for qn, q in sorted(day["questions"].items()):
                if not q["check_count"]:
                    continue
                ref = ROOT / "tools" / "ref" / level / f"d{dn:02d}q{qn}.java"
                if not ref.exists():
                    unverified += 1
                    if args.missing:
                        print(f"unverified  {level} day {dn} Q{qn}  ({q['check_count']} checks)")
                    continue
                cases = q["samples"] + q["hidden"]
                res = runner._execute_batch(ref.read_text(encoding="utf-8"), [c["input"] for c in cases])
                checked += 1
                if res["stage"] != "tests":
                    failures += 1
                    print(f"FAIL  {level} day {dn} Q{qn}: compile error\n{res.get('error', '')[:300]}")
                    continue
                for i, (case, r) in enumerate(zip(cases, res["results"]), start=1):
                    if not (r["ok"] and grading.same(r["stdout"], case["output"])):
                        failures += 1
                        kind = "sample" if i <= len(q["samples"]) else "hidden"
                        print(f"FAIL  {level} day {dn} Q{qn} {kind} #{i}: expected {case['output']!r} got {r['stdout']!r} {r['stderr'][:120]!r}")
    print(f"checked {checked} questions against references, {failures} failing case(s), {unverified} with checks but no reference")
    return 1 if failures else 0


if __name__ == "__main__":
    sys.exit(main())
