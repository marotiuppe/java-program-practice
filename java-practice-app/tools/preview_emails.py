"""Writes every email to an HTML file so you can open it in a browser (and a .txt for the plain-text version).

    python tools/preview_emails.py [output_folder] [base_url]

By default images are loaded from the files in app/static, so no server is needed."""
import os
import pathlib
import sys

ROOT = pathlib.Path(__file__).resolve().parent.parent
sys.path.insert(0, str(ROOT))
from app import email_templates  # noqa: E402

out = pathlib.Path(sys.argv[1] if len(sys.argv) > 1 else ROOT / "email-preview")
# the emails build image URLs as <base>/static/email/<file>, so point base at the app folder
base = sys.argv[2] if len(sys.argv) > 2 else (ROOT / "app").as_uri()
out.mkdir(parents=True, exist_ok=True)

NEXT = {"day": 4, "q": 2, "title": "Relational and Logical Operators with if-else", "category": "Interview-Based Coding"}
UNSUB = "https://example.com/unsubscribe/sample-token"
SAMPLES = {
    "verify": {"link": "https://example.com/verify/sample-token"},
    "reset": {"link": "https://example.com/reset/sample-token"},
    "changed": {},
    "streak": {"streak": 6, "solved_today": 1, "goal": 3, "next": NEXT, "unsub": UNSUB},
    "lapsed": {"solved_total": 23, "next": NEXT, "unsub": UNSUB},
    "weekly": {"solved_week": 9, "streak": 6, "solved_total": 41, "review_due": 3, "next": NEXT, "unsub": UNSUB},
    "test": {},
}
for kind, ctx in SAMPLES.items():
    subject, text, html = email_templates.render(kind, base, **ctx)
    (out / f"{kind}.html").write_text(html, encoding="utf-8")
    (out / f"{kind}.txt").write_text(text, encoding="utf-8")
    print(f"{kind:8} {subject}".encode("ascii", "replace").decode())
print("written to", out)
