"""Merge per-question test fragments into app/content/tests/<level>.json.

Authors (people or agents) write tools/ref/<level>/d<DD>q<N>.tests.json next to the reference solution:
    [{"input": "...", "output": "..."}, ...]     (hidden cases only; the visible samples are not repeated)
This script collects them, replaces that question's entry in the level file, and keeps the rest.
Fragments are left in place so the merge is repeatable.

Usage (from java-practice-app/):   python tools/merge_tests.py [--level beginner]
"""
import argparse
import json
import pathlib
import re

ROOT = pathlib.Path(__file__).resolve().parent.parent
LEVELS = ("beginner", "intermediate", "advanced")
NAME_RE = re.compile(r"^d(\d{2})q(\d)\.tests\.json$")


def main() -> None:
    ap = argparse.ArgumentParser()
    ap.add_argument("--level", choices=LEVELS)
    args = ap.parse_args()
    for level in LEVELS:
        if args.level and level != args.level:
            continue
        out_path = ROOT / "app" / "content" / "tests" / f"{level}.json"
        data = json.loads(out_path.read_text(encoding="utf-8")) if out_path.exists() else {}
        merged = 0
        for frag in sorted((ROOT / "tools" / "ref" / level).glob("*.tests.json")):
            m = NAME_RE.match(frag.name)
            if not m:
                continue
            cases = json.loads(frag.read_text(encoding="utf-8"))
            for c in cases:
                assert set(c) <= {"input", "output"} and "output" in c, f"bad case in {frag.name}"
            data[f"{int(m.group(1))}:{int(m.group(2))}"] = cases
            merged += 1
        if not merged:
            continue
        ordered = dict(sorted(data.items(), key=lambda kv: tuple(int(x) for x in kv[0].split(":"))))
        lines = ["{"]
        keys = list(ordered)
        for ki, key in enumerate(keys):
            lines.append(f'  "{key}": [')
            cs = ordered[key]
            for ci, c in enumerate(cs):
                lines.append("    " + json.dumps(c, ensure_ascii=False) + ("," if ci < len(cs) - 1 else ""))
            lines.append("  ]" + ("," if ki < len(keys) - 1 else ""))
        lines.append("}")
        out_path.write_text("\n".join(lines) + "\n", encoding="utf-8")
        print(f"{level}: merged {merged} fragment(s); {len(ordered)} question(s) with hidden tests")


if __name__ == "__main__":
    main()
