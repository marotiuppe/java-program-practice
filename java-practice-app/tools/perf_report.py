"""Shows what each page costs: database queries, response size (plain and compressed) and render time.

    python tools/perf_report.py

Uses a throwaway SQLite database and a few seeded learners, so numbers are comparable between runs. The query
count matters most in production: every query is a network round trip to the hosted Postgres."""
import os
import sys
import tempfile
import time

sys.path.insert(0, os.path.join(os.path.dirname(os.path.abspath(__file__)), ".."))
os.environ["DB_PATH"] = os.path.join(tempfile.mkdtemp(), "perf.db")
os.environ.pop("DATABASE_URL", None)
os.environ.setdefault("SANDBOX", "off")
os.environ["MAIL_BACKEND"] = "memory"
os.environ["ADMIN_EMAILS"] = "perf@example.com"

from fastapi.testclient import TestClient  # noqa: E402
from app import db, ratelimit  # noqa: E402
from app.main import app  # noqa: E402

counter = {"n": 0}
_orig = db._x


def _counting(c, sql, params=()):
    counter["n"] += 1
    return _orig(c, sql, params)


db._x = _counting

PAGES = ["/", "/dashboard", "/day/1", "/day/1/q/1", "/day/1/q/4", "/profile", "/review", "/account", "/level",
         "/plan/beginner", "/admin", "/admin/users", "/admin/questions", "/static/style.css", "/static/editor.js",
         "/static/javalang.js"]

with TestClient(app, follow_redirects=False) as c:
    ratelimit.reset()
    c.post("/signup", data={"email": "perf@example.com", "password": "password1"})
    c.post("/level", data={"level": "beginner"})
    db.mark_verified(db.get_user_by_email("perf@example.com")["id"])
    for q in range(1, 12):
        c.post("/api/save", json={"day": (q - 1) // 5 + 1, "q": (q - 1) % 5 + 1, "code": "x", "status": "solved", "ran": True})
    print(f"{'page':22} {'queries':>7} {'bytes':>8} {'gzip':>7} {'ms':>6}  cache-control")
    for path in PAGES:
        counter["n"] = 0
        t0 = time.perf_counter()
        r = c.get(path, headers={"Accept-Encoding": "gzip"})
        ms = (time.perf_counter() - t0) * 1000
        wire = r.num_bytes_downloaded  # what actually crossed the network (compressed when the server compresses)
        print(f"{path:22} {counter['n']:7} {len(r.content):8} {wire:7} {ms:6.0f}  {r.headers.get('cache-control', '-')}")
