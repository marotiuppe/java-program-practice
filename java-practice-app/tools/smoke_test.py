"""End-to-end smoke test of the main flows against the real app code (in-process, throwaway database).

Usage (from java-practice-app/, needs JDK 17+ and `pip install -r requirements.txt httpx`):
    python tools/smoke_test.py
Exits non-zero on the first failing check."""
import os
import sys
import tempfile
import time

sys.path.insert(0, os.path.join(os.path.dirname(os.path.abspath(__file__)), ".."))
_tmp = tempfile.mkdtemp()
os.environ["DB_PATH"] = os.path.join(_tmp, "smoke.db")
# Never touch a real database: SQLite by default, or an explicit throwaway Postgres in SMOKE_DATABASE_URL.
if os.environ.get("SMOKE_DATABASE_URL"):
    os.environ["DATABASE_URL"] = os.environ["SMOKE_DATABASE_URL"]
else:
    os.environ.pop("DATABASE_URL", None)
os.environ.setdefault("SANDBOX", "off")  # the sandbox has its own check (tools/check_sandbox.py)
os.environ["MAIL_BACKEND"] = "memory"
os.environ["ADMIN_EMAILS"] = "smoke@example.com"

from fastapi.testclient import TestClient  # noqa: E402
from app import content, db, mailer  # noqa: E402
from app.main import app  # noqa: E402

failures = []


def check(name, ok, detail=""):
    print(("ok    " if ok else "FAIL  ") + name + ("" if ok else f"  -> {detail}"))
    if not ok:
        failures.append(name)


HELLO = 'public class Main { public static void main(String[] a){ System.out.println("hi"); } }'

with TestClient(app, follow_redirects=False) as c:
    # a coding question that has samples/hidden tests to run
    CHK = next((d, q) for d, day in sorted(content.CONTENT["beginner"]["days"].items())
               for q, qq in sorted(day["questions"].items()) if qq["kind"] == "code" and qq["check_count"])
    check("health", c.get("/health").json() == {"status": "ok"})
    check("landing page for visitors", c.get("/").status_code == 200)
    check("public plan page", c.get("/plan/beginner").status_code == 200)

    r = c.post("/signup", data={"email": "smoke@example.com", "password": "password123"})
    check("sign up", r.status_code == 303, r.status_code)
    check("verification email queued", len(mailer.outbox) == 1)
    r = c.post("/level", data={"level": "beginner"})
    check("choose level -> goal page", r.headers.get("location", "").startswith("/goal"), r.headers)
    r = c.post("/goal", data={"goal": 1, "first": "1"})
    check("choose goal -> first question", r.headers.get("location", "").startswith("/day/1/q/1"), r.headers)
    check("dashboard", c.get("/dashboard").status_code == 200)
    check("question page", c.get("/day/1/q/1").status_code == 200)

    time.sleep(1.6)
    r = c.post("/api/run", json={"code": HELLO, "stdin": ""}).json()
    check("run Java", r.get("stage") == "run" and r.get("stdout", "").strip() == "hi", r)
    time.sleep(1.6)
    r = c.post("/api/run", json={"code": "public class Main { int x = ; }", "stdin": ""}).json()
    check("compile error returns help", r.get("stage") == "compile" and "help" in r, r)

    time.sleep(3.2)
    r = c.post("/api/test", json={"day": CHK[0], "q": CHK[1], "code": HELLO}).json()
    check("run samples responds", r.get("stage") == "tests" and r.get("all_passed") is False, r)

    r = c.post("/api/save", json={"day": 1, "q": 1, "code": "saved code", "status": "solved"}).json()
    check("save + mark solved", r.get("ok") is True, r)
    check("saved code is restored", "saved code" in c.get("/day/1/q/1").text)
    check("profile", c.get("/profile").status_code == 200)
    check("review page", c.get("/review").status_code == 200)

    r = c.post("/api/report", json={"day": 1, "q": 1, "code": "x", "category": "other", "note": "smoke"}).json()
    check("report an issue", r.get("ok") is True, r)

    c.post("/account/hints", data={"on": "1"})
    for _ in range(2):
        c.post("/api/save", json={"day": 1, "q": 1, "code": "saved code", "ran": True})
    r = c.post("/api/hint", json={"day": 1, "q": 1}).json()
    check("opt-in hint unlocks after attempts", len(r.get("hints", [])) == 1, r)

    time.sleep(0.5)
    r = c.post("/api/feedback", json={"day": 1, "q": 1, "rating": 1, "difficulty": "hard"}).json()
    check("question feedback is saved", r.get("ok") is True and r.get("difficulty") == "hard", r)

    uid = db.get_user_by_email("smoke@example.com")["id"]
    db.enqueue_review(uid, "beginner", 1, 2, 0)
    check("review queue lists a due question", db.count_due_reviews(uid) == 1)
    r = c.post("/api/review/rate", json={"day": 1, "q": 2, "rating": "easy"}).json()
    check("rating a review reschedules it", r.get("result") == "scheduled", r)

    c.post("/account/reminders", data={"on": "1"})
    check("reminder opt-in stored", db.get_user(uid)["reminders"] == 1)
    r = c.post("/api/predict", json={"day": 1, "q": 4, "answer": "definitely wrong"}).json()
    check("predict answer is checked", r.get("correct") is False, r)

    # admin area: the smoke user is the admin once its email is confirmed
    check("admin pages closed until email is confirmed", c.get("/admin").status_code == 404)
    db.mark_verified(uid)
    for path in ["/admin", "/admin/users", f"/admin/users/{uid}", "/admin/users?q=smoke&sort=solved", "/admin/reports",
                 "/admin/questions", "/admin/questions?level=advanced&sort=reports", "/admin/activity",
                 "/admin/activity?event=signup", "/admin/system"]:
        check(f"admin page {path}", c.get(path).status_code == 200, c.get(path).status_code)

    # shareable achievement cards (roadmap 4.2)
    r = c.post("/api/share/card", json={"kind": "progress"})
    check("share card issued", r.status_code == 200 and "/s/" in r.json().get("url", ""), r.text)
    tok = r.json()["url"].rsplit("/", 1)[1]
    pg = c.get(f"/s/{tok}")
    check("share page has an og:image", pg.status_code == 200 and f"/s/{tok}/og.png" in pg.text, pg.status_code)
    check("share page names the learner from the email", "@smoke" in pg.text, pg.text[:0])
    c.post("/account/name", data={"username": "@Smoke Tester!"})
    tok2 = c.post("/api/share/card", json={"kind": "progress"}).json()["url"].rsplit("/", 1)[1]
    check("chosen username is used", "@SmokeTester" in c.get(f"/s/{tok2}").text)
    c.post("/account/name", data={"username": "", "hide": "1"})
    tok3 = c.post("/api/share/card", json={"kind": "progress"}).json()["url"].rsplit("/", 1)[1]
    check("name can be hidden", "@smoke" not in c.get(f"/s/{tok3}").text.lower().replace("smoke@example.com", ""))
    c.post("/account/name", data={"username": ""})
    check("share images render", c.get(f"/s/{tok}/og.png").headers.get("content-type") == "image/png"
          and c.get(f"/s/{tok}/card.png").status_code == 200)
    check("forged share link is refused", c.get("/s/forged/og.png").status_code == 404)
    check("unearned day card refused", c.post("/api/share/card", json={"kind": "level"}).status_code == 400)

    c.post("/logout")
    r = c.post("/login", data={"email": "smoke@example.com", "password": "password123"})
    check("log in again", r.status_code == 303, r.status_code)
    check("progress survives re-login", "saved code" in c.get("/day/1/q/1").text)
    check("data export", c.get("/account/export").status_code == 200)

print("\nFAILED: " + ", ".join(failures) if failures else "\nAll smoke checks passed.")
sys.exit(1 if failures else 0)
