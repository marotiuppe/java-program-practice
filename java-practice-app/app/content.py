"""Loads the three workbook markdown files into a structure the app can serve."""
import json
import pathlib
import re

import markdown

BASE = pathlib.Path(__file__).parent / "content"

LEVELS = {
    "beginner": ("Beginner", "beginner.md"),
    "intermediate": ("Intermediate", "intermediate.md"),
    "advanced": ("Advanced", "advanced.md"),
}

DAY_RE = re.compile(r"^## Day (\d+) — (.+?)\s*$", re.M)
Q_RE = re.compile(r"^### Question (\d): (.+?)\s*$", re.M)
JAVA_RE = re.compile(r"```java\n(.*?)```", re.S)
BLOCK_RE = re.compile(r"```\n(.*?)```", re.S)
INPUT_RE = re.compile(r"\s*Input:")
OUTPUT_RE = re.compile(r"\s*Output:")

SKELETON = """import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        // write your solution here
    }
}
"""


def _clean(text: str) -> str:
    lines = []
    for line in text.splitlines():
        if line.startswith('<a id="day'):
            continue
        if line.startswith("*End of workbook"):
            continue
        lines.append(line)
    text = "\n".join(lines).strip()
    while text.endswith("---"):
        text = text[:-3].rstrip()
    return text


def _parse_samples(body: str) -> list:
    """Sample cases are fenced blocks holding an `Input:` part followed by an `Output:` part."""
    samples = []
    for block in BLOCK_RE.findall(body):
        lines = block.rstrip("\n").split("\n")
        in_idx = next((i for i, line in enumerate(lines) if INPUT_RE.match(line)), None)
        if in_idx is None:
            continue
        out_idx = next((i for i in range(in_idx + 1, len(lines)) if OUTPUT_RE.match(lines[i])), None)
        if out_idx is None:
            continue
        in_first = INPUT_RE.sub("", lines[in_idx], count=1).strip()
        out_first = OUTPUT_RE.sub("", lines[out_idx], count=1).strip()
        in_lines = ([in_first] if in_first else []) + lines[in_idx + 1:out_idx]
        out_lines = ([out_first] if out_first else []) + lines[out_idx + 1:]
        samples.append({"input": "\n".join(in_lines) + "\n", "output": "\n".join(out_lines)})
    return samples


def _md(text: str) -> str:
    return markdown.markdown(text, extensions=["fenced_code", "tables"])


def _parse_level(path: pathlib.Path) -> dict:
    text = path.read_text(encoding="utf-8")
    day_matches = list(DAY_RE.finditer(text))
    days = {}
    for i, m in enumerate(day_matches):
        end = day_matches[i + 1].start() if i + 1 < len(day_matches) else len(text)
        segment = text[m.end():end]
        q_matches = list(Q_RE.finditer(segment))
        if not q_matches:
            continue
        objectives = _clean(segment[: q_matches[0].start()])
        questions = {}
        for j, qm in enumerate(q_matches):
            q_end = q_matches[j + 1].start() if j + 1 < len(q_matches) else len(segment)
            body = _clean(segment[qm.end():q_end])
            qnum = int(qm.group(1))
            starter = SKELETON
            if qnum in (4, 5):
                code = JAVA_RE.search(body)
                if code:
                    starter = code.group(1)
            questions[qnum] = {
                "q": qnum,
                "category": qm.group(2),
                "html": _md(body),
                "starter": starter,
                "kind": {4: "predict", 5: "debug"}.get(qnum, "code"),
                "samples": _parse_samples(body) if qnum <= 3 else [],
            }
        day = int(m.group(1))
        days[day] = {
            "n": day,
            "title": m.group(2),
            "objectives_html": _md(objectives),
            "questions": questions,
        }
    return days


CONTENT = {}


def _attach_tests(level: str, days: dict) -> None:
    """Hidden test cases live in content/tests/<level>.json as {"<day>:<q>": [{"input", "output"}, ...]}.
    They are used by "Run samples/checks" for coding questions and as the checks for Debug questions."""
    path = BASE / "tests" / f"{level}.json"
    data = json.loads(path.read_text(encoding="utf-8")) if path.exists() else {}
    for dn, day in days.items():
        for qn, q in day["questions"].items():
            cases = []
            for c in data.get(f"{dn}:{qn}", []):
                inp = c.get("input", "")
                if inp and not inp.endswith("\n"):
                    inp += "\n"
                cases.append({"input": inp, "output": c["output"]})
            q["hidden"] = cases
            q["check_count"] = len(q["samples"]) + len(cases)


def load() -> None:
    for key, (label, filename) in LEVELS.items():
        days = _parse_level(BASE / filename)
        _attach_tests(key, days)
        CONTENT[key] = {"label": label, "days": days}
