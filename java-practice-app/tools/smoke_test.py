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
    check("leaderboard page", c.get("/leaderboard").status_code == 200)
    c.post("/account/leaderboard", data={"on": "1", "back": "/leaderboard"})
    check("joined learner is listed", "(you)" in c.get("/leaderboard").text or "Solve a question" in c.get("/leaderboard").text)
    c.post("/account/leaderboard", data={"back": "/leaderboard"})
    check("left learner is not listed", "(you)" not in c.get("/leaderboard").text)
    # invites (roadmap 4.4): link, banner, reward on the invitee's first solved question
    import re as _re
    inv = c.get("/invite")
    mm = _re.search(r"/join/([a-z0-9]+)", inv.text)
    check("invite page with a personal link", inv.status_code == 200 and bool(mm), inv.status_code)
    code = mm.group(1)
    friend = TestClient(app, follow_redirects=False)
    check("invite link leads to signup", friend.get(f"/join/{code}").headers.get("location") == f"/signup?ref={code}")
    check("signup page names the inviter", "invited you" in friend.get(f"/signup?ref={code}").text)
    friend.post("/signup", data={"email": "friend-smoke@example.com", "password": "password123"})
    fid = db.get_user_by_email("friend-smoke@example.com")
    check("invitee is linked to the inviter", fid["referred_by"] == uid)
    from app import main as _m, stats as _stats
    db.save_progress(fid["id"], "beginner", 1, 1, "x", "solved", True)
    fb = _m._solve_feedback(db.get_user(fid["id"]), 1, 1)
    check("first solve rewards both with a freeze", bool(fb and fb.get("referral")) and db.get_user(uid)["freeze_bonus"] != ""
          and db.get_user(fid["id"])["freeze_bonus"] != "", fb)
    check("share card" + " carries the invite code", "r" in __import__("app.sharecard", fromlist=["x"]).read_token(tok))
    # public profile (roadmap 4.1)
    anon = TestClient(app, follow_redirects=False)
    check("profile is private by default", anon.get("/u/smoketest").status_code == 404)
    c.post("/account/profile", data={"handle": "SmokeTest", "on": "1"})
    pp = anon.get("/u/smoketest")
    check("public profile after opting in", pp.status_code == 200 and "@smoketest" in pp.text and "smoke@example.com" not in pp.text)
    check("profile preview image renders", anon.get("/u/smoketest/og.png").headers.get("content-type") == "image/png")
    c.post("/account/profile", data={"handle": "smoketest"})
    check("profile can be switched off", anon.get("/u/smoketest").status_code == 404)
    check("share images render", c.get(f"/s/{tok}/og.png").headers.get("content-type") == "image/png"
          and c.get(f"/s/{tok}/card.png").status_code == 200)
    check("forged share link is refused", c.get("/s/forged/og.png").status_code == 404)
    check("unearned day card refused", c.post("/api/share/card", json={"kind": "level"}).status_code == 400)

    c.post("/logout")
    r = c.post("/login", data={"email": "smoke@example.com", "password": "password123"})
    check("log in again", r.status_code == 303, r.status_code)
    check("progress survives re-login", "saved code" in c.get("/day/1/q/1").text)
    check("data export", c.get("/account/export").status_code == 200)

    # certificates (roadmap 4.3): locked until a level is finished, then claimable and publicly verifiable
    check("certificates page", c.get("/certificate").status_code == 200)
    check("cannot claim an unfinished level", c.post("/certificate/beginner", data={"name": "Smoke Tester"}).headers["location"].startswith("/certificate?error="))
    for d_, day_ in content.CONTENT["beginner"]["days"].items():
        for q_ in day_["questions"]:
            db.save_progress(uid, "beginner", d_, q_, "x", "solved", True)
    cr = c.post("/certificate/beginner", data={"name": "Smoke Tester"})
    cert_url = cr.headers["location"]
    check("claim after finishing the level", cr.status_code == 303 and cert_url.startswith("/cert/"), cert_url)
    check("public certificate page and files", anon.get(cert_url).status_code == 200 and anon.get(cert_url + ".png").headers["content-type"] == "image/png"
          and anon.get(cert_url + ".pdf").content[:5] == b"%PDF-")
    c.post("/certificate/beginner/delete")
    check("deleted certificate disappears", anon.get(cert_url).status_code == 404)

    # mock interview (roadmap 3.6): three mixed questions from finished days, timed, graded
    check("interview intro", c.get("/interview").status_code == 200 and "Start a" in c.get("/interview").text)
    iv = c.post("/interview/start")
    iv_url = iv.headers["location"]
    check("interview starts", iv.status_code == 303 and iv_url.startswith("/interview/"), iv_url)
    live = c.get(iv_url)
    check("interview page has three questions and a timer", live.status_code == 200 and live.text.count('role="tab"') == 3 and "iv-timer" in live.text)
    fin = c.post(iv_url.replace("/interview/", "/api/interview/") + "/finish", json={"answers": {}})
    check("interview finishes and is graded", fin.json().get("url") == iv_url and "iv-score" in c.get(iv_url).text)

    # community (roadmap 4.9): changelog, ideas with votes, question of the week
    check("changelog is public", anon.get("/changelog").status_code == 200 and "What" in anon.get("/changelog").text)
    check("ideas page is public", anon.get("/ideas").status_code == 200)
    check("anonymous cannot post or vote", anon.post("/ideas", data={"title": "A public idea here"}).headers["location"] == "/login"
          and anon.post("/ideas/1/vote").headers["location"] == "/login")
    check("short idea title refused", c.post("/ideas", data={"title": "no"}).headers["location"] == "/ideas?msg=short")
    posted = c.post("/ideas", data={"title": "Dark editor theme please", "body": "Easier on the eyes at night"})
    check("idea posted", posted.status_code == 303 and "msg=posted" in posted.headers["location"], posted.headers.get("location"))
    page = anon.get("/ideas").text
    check("idea listed without the author's email", "Dark editor theme please" in page and "smoke@example.com" not in page)
    iid = db.list_ideas()[0]["id"]
    check("author's vote counted", db.list_ideas()[0]["votes"] == 1)
    c.post(f"/ideas/{iid}/vote", data={"sort": "top"})
    check("vote toggles off", db.list_ideas()[0]["votes"] == 0)
    c.post(f"/ideas/{iid}/vote", data={"sort": "top"})
    check("vote toggles on", db.list_ideas()[0]["votes"] == 1)
    for n in range(2):
        c.post("/ideas", data={"title": f"Another idea number {n}"})
    check("daily idea limit", c.post("/ideas", data={"title": "One idea too many"}).headers["location"] == "/ideas?msg=limit")
    check("admin can hide an idea", c.post(f"/admin/ideas/{iid}/status", data={"status": "hidden"}).status_code == 303
          and "Dark editor theme please" not in anon.get("/ideas").text and c.get("/admin/ideas").status_code == 200)
    check("question of the week on the dashboard", "Question of the week" in c.get("/dashboard").text)
    check("community pages in sitemap", "/changelog" in anon.get("/sitemap.xml").text and "/ideas" in anon.get("/sitemap.xml").text)

print("\nFAILED: " + ", ".join(failures) if failures else "\nAll smoke checks passed.")
sys.exit(1 if failures else 0)
