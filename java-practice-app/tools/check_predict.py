"""Runs every Predict-the-output snippet twice and fails if one does not compile, crashes, prints nothing,
or prints different output on the two runs (a flaky answer would make "Check answer" unfair).

Usage (from java-practice-app/, needs JDK 17+):  python tools/check_predict.py [--level beginner]"""
import argparse
import os
import sys

sys.path.insert(0, os.path.join(os.path.dirname(os.path.abspath(__file__)), ".."))
os.environ.setdefault("SANDBOX", "off")
from app import content, runner  # noqa: E402


def main() -> int:
    ap = argparse.ArgumentParser()
    ap.add_argument("--level", choices=list(content.LEVELS))
    args = ap.parse_args()
    content.load()
    bad, total = [], 0
    for level, info in content.CONTENT.items():
        if args.level and level != args.level:
            continue
        for dn, day in sorted(info["days"].items()):
            for qn, q in sorted(day["questions"].items()):
                if q["kind"] != "predict":
                    continue
                total += 1
                first = runner._execute(q["starter"], "")
                second = runner._execute(q["starter"], "")
                label = f"{level} day {dn} Q{qn}"
                if first.get("stage") != "run" or not first.get("ok"):
                    bad.append((label, f"did not run cleanly: {first.get('error') or first.get('stderr') or first}"))
                elif not first["stdout"].strip():
                    bad.append((label, "prints nothing"))
                elif first["stdout"] != second.get("stdout"):
                    bad.append((label, "output differs between two runs (non-deterministic)"))
    for label, why in bad:
        print("FAIL", label, "-", why)
    print(f"{total - len(bad)}/{total} predict snippets are stable")
    return 1 if bad else 0


if __name__ == "__main__":
    sys.exit(main())
