"""Mock interview (roadmap 3.6): three mixed questions from days the learner has fully solved, timed, graded
automatically. This module holds the pure parts (what can be asked, how to pick, how to score); the web routes
live in main.py."""
import os
import random

from . import content, grading
from .runner import run_java_batch

MINUTES = int(os.environ.get("INTERVIEW_MINUTES", "30"))
GRACE = 20            # seconds after the deadline during which a late save or submit still counts (network lag)
PER_DAY_LIMIT = 5     # interviews a learner can start in 24 hours
KINDS = ("code", "debug", "predict")
MAX_CODE = 50_000


def finished_days(rows) -> set:
    """{(level, day)} where every question is solved. rows: (level, day, qnum, status) tuples."""
    solved = {}
    for level, day, qn, status in rows:
        if status == "solved":
            solved.setdefault((level, day), set()).add(qn)
    return {(lv, d) for (lv, d), qs in solved.items()
            if lv in content.CONTENT and d in content.CONTENT[lv]["days"]
            and len(qs) >= len(content.CONTENT[lv]["days"][d]["questions"])}


def pool(days: set) -> dict:
    """What can be asked from these finished days: coding and debug questions that have checks, and predict
    questions that have a known answer. {"code": [ref, ...], "debug": [...], "predict": [...]}"""
    out = {k: [] for k in KINDS}
    for level, day in sorted(days):
        for qn, q in sorted(content.CONTENT[level]["days"][day]["questions"].items()):
            kind = q["kind"]
            if kind in ("code", "debug") and not q["check_count"]:
                continue
            if kind == "predict" and not q.get("expected"):
                continue
            out[kind].append({"l": level, "d": day, "q": qn})
    return out


def pick(available: dict, recent: set, rng=random):
    """Three refs, one of each kind when possible. Questions asked in the last sessions (`recent` is a set of
    (level, day, q)) are avoided while there are alternatives. None when fewer than three questions exist."""
    key = lambda r: (r["l"], r["d"], r["q"])
    chosen = []
    for kind in KINDS:
        fresh = [r for r in available[kind] if key(r) not in recent]
        options = fresh or available[kind]
        if options:
            chosen.append(rng.choice(options))
    if len(chosen) < 3:  # a kind is missing: fill up from whatever else is left
        left = [r for kind in KINDS for r in available[kind] if key(r) not in {key(c) for c in chosen}]
        fresh = [r for r in left if key(r) not in recent]
        rng.shuffle(fresh)
        rng.shuffle(left)
        for r in fresh + [r for r in left if r not in fresh]:
            if len(chosen) == 3:
                break
            chosen.append(r)
    if len(chosen) < 3:
        return None
    rng.shuffle(chosen)
    return chosen


def question(ref):
    """The question dict for a stored ref, or None if the content no longer has it."""
    try:
        return content.CONTENT[ref["l"]]["days"][ref["d"]]["questions"][ref["q"]]
    except KeyError:
        return None


def describe(ref) -> dict:
    """What the live page needs about one question (never the answers or the hidden tests)."""
    q = question(ref)
    return {"l": ref["l"], "d": ref["d"], "q": ref["q"], "kind": q["kind"], "title": q["category"], "html": q["html"],
            "starter": q["starter"] if q["kind"] != "predict" else "", "samples": len(q["samples"]) if q["kind"] == "code" else 0}


def clean_answer(raw) -> dict:
    """Keep only the fields we store, with sane sizes."""
    raw = raw if isinstance(raw, dict) else {}
    out = {}
    for key, limit in (("c", MAX_CODE), ("a", 5_000), ("s", 5_000)):
        if isinstance(raw.get(key), str):
            out[key] = raw[key][:limit]
    return out


def _title(kind: str) -> str:
    return {"code": "Coding", "debug": "Debug the code", "predict": "Predict the output"}[kind]


kind_title = _title


async def grade(refs: list, answers: dict):
    """(score 0-100, breakdown) for the stored answers. Coding and debug questions run against every check
    (partial credit by checks passed); predict questions are right or wrong."""
    breakdown = []
    for i, ref in enumerate(refs):
        q = question(ref)
        ans = answers.get(str(i), {})
        item = {"i": i, "l": ref["l"], "d": ref["d"], "q": ref["q"], "kind": q["kind"] if q else "code",
                "title": q["category"] if q else "Question", "pct": 0, "passed": 0, "total": 0, "note": "", "answered": False}
        if q is None:
            breakdown.append(item)
            continue
        if q["kind"] == "predict":
            given = ans.get("a", "")
            item.update(answered=bool(given.strip()), total=1, expected=q["expected"])
            if grading.same(given, q["expected"]):
                item.update(pct=100, passed=1)
            else:
                item["note"] = "Not the exact output." if given.strip() else "No answer given."
        else:
            code = ans.get("c", "")
            cases = q["samples"] + q["hidden"]
            item.update(answered=bool(code.strip()) and code.strip() != q["starter"].strip(), total=len(cases))
            if not item["answered"]:
                item["note"] = "No changes made." if code.strip() else "No code written."
            else:
                res = await run_java_batch(code, [c["input"] for c in cases])
                if res.get("stage") != "tests":
                    item["note"] = (res.get("error") or "The program did not compile.").strip()[:300]
                else:
                    ok = sum(1 for case, r in zip(cases, res["results"]) if r["ok"] and grading.same(r["stdout"], case["output"]))
                    item.update(passed=ok, pct=round(100 * ok / len(cases)) if cases else 0)
                    if ok < len(cases):
                        item["note"] = f"{len(cases) - ok} of {len(cases)} checks failed."
        breakdown.append(item)
    score = round(sum(b["pct"] for b in breakdown) / len(breakdown)) if breakdown else 0
    return score, breakdown


def verdict(score: int) -> str:
    return "Interview ready" if score >= 85 else "Solid" if score >= 70 else "Getting there" if score >= 40 else "Keep practising"
