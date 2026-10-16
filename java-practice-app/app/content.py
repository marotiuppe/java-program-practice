"""Loads the three workbook markdown files into a structure the app can serve."""
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


def load() -> None:
    for key, (label, filename) in LEVELS.items():
        CONTENT[key] = {"label": label, "days": _parse_level(BASE / filename)}
