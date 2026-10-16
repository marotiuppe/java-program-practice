"""Predict-the-output answers: generates them and keeps them honest.

    python tools/check_predict.py            # CHECK: run every snippet twice; fail if one does not compile, crashes,
                                             #        prints nothing, is non-deterministic, or differs from the saved answer
    python tools/check_predict.py --write    # WRITE: regenerate app/content/predict_outputs/<level>.json
    python tools/check_predict.py --level beginner

The app uses the saved answers, so checking an answer is instant and needs no Java run. Run --write whenever a
Predict snippet changes (needs JDK 17+; use the Docker image or JDK 21 so output matches production)."""
import argparse
import json
import os
import sys

HERE = os.path.dirname(os.path.abspath(__file__))
sys.path.insert(0, os.path.join(HERE, ".."))
os.environ.setdefault("SANDBOX", "off")
from app import content, runner  # noqa: E402

OUT_DIR = os.path.join(HERE, "..", "app", "content", "predict_outputs")


def main() -> int:
    ap = argparse.ArgumentParser()
    ap.add_argument("--level", choices=list(content.LEVELS))
    ap.add_argument("--write", action="store_true", help="regenerate the saved answers")
    args = ap.parse_args()
    content.load()
    bad, total, computed = [], 0, {}
    for level, info in content.CONTENT.items():
        if args.level and level != args.level:
            continue
        computed[level] = {}
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
                else:
                    computed[level][f"{dn}:{qn}"] = first["stdout"]
                    saved = q.get("expected")
                    if not args.write and saved != first["stdout"]:
                        bad.append((label, "saved answer is missing or stale; run: python tools/check_predict.py --write"))
    for label, why in bad:
        print("FAIL", label, "-", why)
    if args.write and not bad:
        os.makedirs(OUT_DIR, exist_ok=True)
        for level, answers in computed.items():
            with open(os.path.join(OUT_DIR, f"{level}.json"), "w", encoding="utf-8", newline="\n") as f:
                json.dump(answers, f, indent=1, ensure_ascii=False, sort_keys=True)
                f.write("\n")
        print(f"wrote {sum(len(a) for a in computed.values())} answers to {os.path.normpath(OUT_DIR)}")
    print(f"{total - len(bad)}/{total} predict snippets are stable" + ("" if args.write else " and match the saved answers"))
    return 1 if bad else 0


if __name__ == "__main__":
    sys.exit(main())
