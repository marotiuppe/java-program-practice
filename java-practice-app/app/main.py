import asyncio
import json
import hashlib
import logging
import os
import re
import time
import urllib.request
from datetime import date, datetime, timedelta
from contextlib import asynccontextmanager

from fastapi import BackgroundTasks, FastAPI, Form, Request
from fastapi.responses import FileResponse, JSONResponse, RedirectResponse, Response
from fastapi.staticfiles import StaticFiles
from fastapi.templating import Jinja2Templates
from starlette.middleware.sessions import SessionMiddleware

from . import badges, content, db, grading, hints, mailer, ratelimit, reminders, sandbox, stats
from .runner import run_java, run_java_batch, sandbox_self_test

logging.basicConfig(level=logging.INFO)
log = logging.getLogger("app")
HERE = os.path.dirname(__file__)
templates = Jinja2Templates(directory=os.path.join(HERE, "templates"))

EMAIL_RE = re.compile(r"^[^@\s]+@[^@\s]+\.[^@\s]+$")
ADMIN_EMAILS = {e.strip().lower() for e in os.environ.get("ADMIN_EMAILS", "").split(",") if e.strip()}
REPORT_CATEGORIES = {"wrong_sample", "unclear", "broken_code", "other"}
STATUSES = {"todo", "attempted", "solved"}
KEEPALIVE_SECONDS = int(os.environ.get("KEEPALIVE_SECONDS", "300"))
_last_run = {}  # user_id -> timestamp, for a simple per-user rate limit


def _ping(url: str) -> None:
    try:
        with urllib.request.urlopen(url, timeout=20) as r:
            log.info("keep-alive %s -> %s", url, r.status)
    except Exception as exc:  # never crash the loop
        log.warning("keep-alive failed: %s", exc)


async def _keepalive_loop(url: str) -> None:
    # Calls our own PUBLIC url so the request enters through Render's edge and counts
    # as inbound traffic. Calling localhost would not prevent the free-plan sleep.
    while True:
        await asyncio.sleep(KEEPALIVE_SECONDS)
        await asyncio.to_thread(_ping, url)


async def _reminder_loop() -> None:
    """Checks every CHECK_SECONDS whether any reminder email is due (see app/reminders.py)."""
    await asyncio.sleep(60)
    while True:
        base = (os.environ.get("APP_BASE_URL") or os.environ.get("RENDER_EXTERNAL_URL") or "").rstrip("/")
        if base:
            try:
                await asyncio.to_thread(reminders.run_once, base)
            except Exception:
                log.exception("reminder run failed")
        else:
            log.warning("reminders skipped: set APP_BASE_URL so email links point at the site")
        await asyncio.sleep(reminders.CHECK_SECONDS)


@asynccontextmanager
async def lifespan(app: FastAPI):
    db.init()
    content.load()
    await asyncio.to_thread(sandbox_self_test)
    task = None
    reminder_task = asyncio.create_task(_reminder_loop()) if mailer.configured() else None
    base = os.environ.get("KEEPALIVE_URL") or os.environ.get("RENDER_EXTERNAL_URL")
    if base:
        task = asyncio.create_task(_keepalive_loop(base.rstrip("/") + "/health"))
    yield
    if task:
        task.cancel()
    if reminder_task:
        reminder_task.cancel()


app = FastAPI(lifespan=lifespan)
app.add_middleware(
    SessionMiddleware,
    secret_key=os.environ.get("SECRET_KEY", "dev-only-change-me"),
    same_site="lax",
    https_only=bool(os.environ.get("RENDER")),
    max_age=60 * 60 * 24 * 30,
)
app.mount("/static", StaticFiles(directory=os.path.join(HERE, "static")), name="static")


@app.exception_handler(Exception)
async def unhandled_error(request: Request, exc: Exception):
    """Leave a findable trace (method, path, user) for every 500."""
    log.error("500 %s %s uid=%s", request.method, request.url.path, request.session.get("uid"), exc_info=exc)
    if request.url.path.startswith("/api/"):
        return JSONResponse({"error": "Something went wrong on the server."}, status_code=500)
    return JSONResponse({"error": "Something went wrong on the server. Please try again."}, status_code=500)


@app.middleware("http")
async def user_timezone(request: Request, call_next):
    """The browser sends its timezone in a cookie; "today" and streaks then follow the learner's clock."""
    stats.set_request_timezone(request.cookies.get("tz"))
    return await call_next(request)


def current_user(request: Request):
    uid = request.session.get("uid")
    if not uid:
        return None
    user = db.get_user(uid)
    if user is None:
        request.session.clear()
        return None
    # A password change (reset) invalidates sessions created before it.
    if "pwv" in request.session and request.session["pwv"] != _pwv(user["pw_hash"]):
        request.session.clear()
        return None
    return user


def _pwv(pw_hash: str) -> str:
    return hashlib.sha256(pw_hash.encode()).hexdigest()[:12]


def _login_session(request: Request, user) -> None:
    request.session["uid"] = user["id"]
    request.session["pwv"] = _pwv(user["pw_hash"])


def base_url(request: Request) -> str:
    return (os.environ.get("APP_BASE_URL") or os.environ.get("RENDER_EXTERNAL_URL") or str(request.base_url)).rstrip("/")


VERIFY_TTL = 24 * 3600
RESET_TTL = 30 * 60
FORGOT_IP = (5, 3600)
FORGOT_EMAIL = (3, 3600)
RESEND_USER = (3, 3600)


def _send_verification(email: str, link: str) -> None:
    mailer.send(email, "Confirm your email for Java Practice",
                f"Welcome to Java Practice!\n\nConfirm your email address by opening this link (valid for 24 hours):\n{link}\n\n"
                "If you did not create an account, you can ignore this message.\n")


def _send_reset(email: str, link: str) -> None:
    mailer.send(email, "Reset your Java Practice password",
                f"Someone (hopefully you) asked to reset the password for this account.\n\n"
                f"Open this link within 30 minutes to choose a new password:\n{link}\n\n"
                "If you did not ask for this, ignore this message. Your password stays the same.\n")


def _send_password_changed(email: str) -> None:
    mailer.send(email, "Your Java Practice password was changed",
                "The password for your Java Practice account was just changed.\n\n"
                "If this was not you, reset your password again right away from the login page.\n")


def is_admin(user) -> bool:
    return bool(user) and user["email"].lower() in ADMIN_EMAILS


def render(request: Request, name: str, status_code: int = 200, **ctx):
    return templates.TemplateResponse(
        request, name, {"user": (u := current_user(request)), "is_admin": is_admin(u), **ctx}, status_code=status_code)


def client_ip(request: Request) -> str:
    """Best guess at the caller's IP. Behind Render's proxy the left-most X-Forwarded-For entry is the client.
    A client can fake that header, so per-IP limits slow abuse down but the per-account limits are the real guard."""
    fwd = request.headers.get("x-forwarded-for", "")
    if fwd:
        return fwd.split(",")[0].strip()
    return request.client.host if request.client else "unknown"


# Limits: (attempts, window in seconds)
SIGNUP_IP = (5, 3600)
LOGIN_FAIL_IP = (20, 900)
LOGIN_FAIL_EMAIL = (5, 900)
RUN_IP = (60, 60)


def _run_limited(request: Request):
    """429 response when this IP is running code too fast, else None."""
    wait = ratelimit.hit(f"run:{client_ip(request)}", *RUN_IP)
    if wait:
        return JSONResponse({"stage": "busy", "ok": False, "error": f"Too many runs from your network. Try again in {ratelimit.human(wait)}."},
                            status_code=429)
    return None


@app.get("/favicon.ico", include_in_schema=False)
async def favicon():
    return RedirectResponse("/static/favicon.svg", status_code=301)


@app.get("/sw.js", include_in_schema=False)
async def service_worker():
    # Served from the site root so its scope covers every page (a file under /static/ could not).
    return FileResponse(os.path.join(HERE, "static", "sw.js"), media_type="application/javascript",
                        headers={"Cache-Control": "no-cache"})


@app.get("/health")
async def health():
    return {"status": "ok"}


@app.get("/")
async def index(request: Request):
    user = current_user(request)
    if not user:
        return RedirectResponse("/login", status_code=303)
    if not user["level"]:
        return RedirectResponse("/level", status_code=303)
    return RedirectResponse("/dashboard", status_code=303)


@app.get("/signup")
async def signup_page(request: Request):
    return render(request, "auth.html", mode="signup", error=None)


@app.post("/signup")
async def signup(request: Request, background: BackgroundTasks, email: str = Form(...), password: str = Form(...)):
    email = email.strip().lower()
    wait = ratelimit.hit(f"signup:{client_ip(request)}", *SIGNUP_IP)
    if wait:
        return render(request, "auth.html", status_code=429, mode="signup",
                      error=f"Too many sign-ups from your network. Try again in {ratelimit.human(wait)}.")
    if not EMAIL_RE.match(email):
        return render(request, "auth.html", mode="signup", error="Enter a valid email address.")
    if len(password) < 8:
        return render(request, "auth.html", mode="signup", error="Password must be at least 8 characters.")
    uid = db.create_user(email, password, verified=not mailer.configured())
    if uid is None:
        return render(request, "auth.html", mode="signup", error="That email is already registered.")
    _login_session(request, db.get_user(uid))
    db.log_event(uid, "signup")
    if mailer.configured():
        token = db.create_token(uid, "verify", VERIFY_TTL)
        background.add_task(_send_verification, email, f"{base_url(request)}/verify/{token}")
    return RedirectResponse("/level", status_code=303)


@app.get("/login")
async def login_page(request: Request):
    notice = "Password changed. Log in with your new password." if request.query_params.get("reset") else None
    return render(request, "auth.html", mode="login", error=None, notice=notice)


@app.post("/login")
async def login(request: Request, email: str = Form(...), password: str = Form(...)):
    email = email.strip().lower()
    ip_key, email_key = f"login-fail:ip:{client_ip(request)}", f"login-fail:email:{email}"
    wait = max(ratelimit.retry_after(ip_key, *LOGIN_FAIL_IP), ratelimit.retry_after(email_key, *LOGIN_FAIL_EMAIL))
    if wait:
        return render(request, "auth.html", status_code=429, mode="login",
                      error=f"Too many failed attempts. Try again in {ratelimit.human(wait)}.")
    user = db.get_user_by_email(email)
    if not user or not db.verify_password(password, user["pw_hash"]):
        ratelimit.record(ip_key)
        ratelimit.record(email_key)
        return render(request, "auth.html", mode="login", error="Wrong email or password.")
    _login_session(request, user)
    db.log_event(user["id"], "return_visit", stats.today_str(), once=True)
    return RedirectResponse("/", status_code=303)


@app.get("/forgot")
async def forgot_page(request: Request):
    return render(request, "forgot.html", error=None, sent=False)


@app.post("/forgot")
async def forgot(request: Request, background: BackgroundTasks, email: str = Form(...)):
    if not mailer.configured():
        return render(request, "forgot.html", error="Password reset by email is not set up on this server yet.", sent=False)
    email = email.strip().lower()
    wait = ratelimit.hit(f"forgot:ip:{client_ip(request)}", *FORGOT_IP)
    if wait:
        return render(request, "forgot.html", status_code=429, sent=False,
                      error=f"Too many requests. Try again in {ratelimit.human(wait)}.")
    # Same answer whether or not the account exists, and whether or not this email hit its own limit.
    if not ratelimit.hit(f"forgot:email:{email}", *FORGOT_EMAIL):
        user = db.get_user_by_email(email)
        if user:
            token = db.create_token(user["id"], "reset", RESET_TTL)
            background.add_task(_send_reset, email, f"{base_url(request)}/reset/{token}")
            log.info("password reset requested for user %s", user["id"])
    return render(request, "forgot.html", error=None, sent=True)


@app.get("/reset/{token}")
async def reset_page(request: Request, token: str):
    if db.peek_token(token, "reset") is None:
        return render(request, "message.html", status_code=400, title="Link expired",
                      text="This reset link is invalid, expired or already used.", href="/forgot", link="Request a new link")
    return render(request, "reset.html", token=token, error=None)


@app.post("/reset/{token}")
async def reset(request: Request, background: BackgroundTasks, token: str, password: str = Form(...)):
    if len(password) < 8:
        return render(request, "reset.html", status_code=400, token=token, error="Password must be at least 8 characters.")
    uid = db.consume_token(token, "reset")
    if uid is None:
        return render(request, "message.html", status_code=400, title="Link expired",
                      text="This reset link is invalid, expired or already used.", href="/forgot", link="Request a new link")
    db.set_password(uid, password)
    user = db.get_user(uid)
    db.mark_verified(uid)  # they proved they control the mailbox
    request.session.clear()
    background.add_task(_send_password_changed, user["email"])
    return RedirectResponse("/login?reset=1", status_code=303)


@app.get("/verify/{token}")
async def verify(request: Request, token: str):
    uid = db.consume_token(token, "verify")
    if uid is None:
        return render(request, "message.html", status_code=400, title="Link expired",
                      text="This confirmation link is invalid, expired or already used. Log in and request a new one.",
                      href="/login", link="Log in")
    db.mark_verified(uid)
    return render(request, "message.html", title="Email confirmed", text="Thanks, your email address is confirmed.",
                  href="/dashboard", link="Continue")


@app.post("/verify/resend")
async def verify_resend(request: Request, background: BackgroundTasks):
    user = current_user(request)
    if not user:
        return RedirectResponse("/login", status_code=303)
    if not user["email_verified"] and mailer.configured() and not ratelimit.hit(f"resend:{user['id']}", *RESEND_USER):
        token = db.create_token(user["id"], "verify", VERIFY_TTL)
        background.add_task(_send_verification, user["email"], f"{base_url(request)}/verify/{token}")
    return RedirectResponse("/dashboard?sent=1", status_code=303)


@app.get("/privacy")
async def privacy(request: Request):
    return render(request, "privacy.html")


@app.get("/account")
async def account_page(request: Request):
    user = current_user(request)
    if not user:
        return RedirectResponse("/login", status_code=303)
    return render(request, "account.html", error=None, saved=bool(request.query_params.get("saved")),
                  reminders_on=bool(user["reminders"]), mail_ready=mailer.configured(), verified=bool(user["email_verified"]))


@app.get("/account/export")
async def account_export(request: Request):
    user = current_user(request)
    if not user:
        return RedirectResponse("/login", status_code=303)
    data = db.export_user(user["id"])
    return Response(json.dumps(data, indent=2), media_type="application/json",
                    headers={"Content-Disposition": 'attachment; filename="java-practice-data.json"'})


DELETE_FAIL = (5, 900)


@app.post("/account/delete")
async def account_delete(request: Request, password: str = Form(...)):
    user = current_user(request)
    if not user:
        return RedirectResponse("/login", status_code=303)
    key = f"delete-fail:{user['id']}"
    wait = ratelimit.retry_after(key, *DELETE_FAIL)
    if wait:
        return render(request, "account.html", status_code=429, saved=False, reminders_on=bool(user["reminders"]),
                      mail_ready=mailer.configured(), verified=bool(user["email_verified"]),
                      error=f"Too many wrong passwords. Try again in {ratelimit.human(wait)}.")
    if not db.verify_password(password, user["pw_hash"]):
        ratelimit.record(key)
        return render(request, "account.html", status_code=400, saved=False, reminders_on=bool(user["reminders"]),
                      mail_ready=mailer.configured(), verified=bool(user["email_verified"]),
                      error="That password is not correct.")
    db.delete_user(user["id"])
    request.session.clear()
    return render(request, "message.html", title="Account deleted",
                  text="Your account and all of its data have been permanently deleted.", href="/signup", link="Create a new account")


@app.post("/account/reminders")
async def account_reminders(request: Request, on: str = Form("")):
    user = current_user(request)
    if not user:
        return RedirectResponse("/login", status_code=303)
    db.set_reminders(user["id"], bool(on))
    return RedirectResponse("/account?saved=1", status_code=303)


async def _unsubscribe(request: Request, token: str):
    uid = reminders.read_unsubscribe_token(token)
    if uid is None:
        return render(request, "message.html", status_code=400, title="Link not valid",
                      text="This unsubscribe link is not valid. You can turn reminders off from your account page.",
                      href="/account", link="Account settings")
    db.set_reminders(uid, False)
    return render(request, "message.html", title="You are unsubscribed",
                  text="We will not send you reminder emails any more. You can turn them back on from your account page.",
                  href="/", link="Back to Java Practice")


@app.get("/unsubscribe/{token}")
async def unsubscribe_get(request: Request, token: str):
    return await _unsubscribe(request, token)


@app.post("/unsubscribe/{token}")  # mail clients' one-click unsubscribe (RFC 8058) POST here
async def unsubscribe_post(request: Request, token: str):
    return await _unsubscribe(request, token)


@app.post("/logout")
async def logout(request: Request):
    request.session.clear()
    return RedirectResponse("/login", status_code=303)


@app.get("/level")
async def level_page(request: Request):
    user = current_user(request)
    if not user:
        return RedirectResponse("/login", status_code=303)
    return render(request, "level.html", levels=content.LEVELS, current=user["level"])


@app.post("/level")
async def level_set(request: Request, level: str = Form(...)):
    user = current_user(request)
    if not user:
        return RedirectResponse("/login", status_code=303)
    if level in content.LEVELS:
        first_time = not user["level"]
        db.set_level(user["id"], level)
        if first_time:  # onboarding: pick a daily goal, then straight to the first question
            return RedirectResponse("/goal?first=1", status_code=303)
    return RedirectResponse("/dashboard", status_code=303)


@app.post("/streak/repair")
async def streak_repair(request: Request):
    """Once a month, count yesterday as practiced so a one-day slip does not wipe the streak."""
    user = current_user(request)
    if not user:
        return RedirectResponse("/login", status_code=303)
    active = db.get_active_dates(user["id"])
    if _repair_streak_length(active) and db.log_event(
            user["id"], "streak_repair", stats.today_str()[:7], once=True):
        db.touch_activity(user["id"], (stats.today() - timedelta(days=1)).isoformat())
    return RedirectResponse("/dashboard", status_code=303)


GOALS = (1, 3, 5)


@app.get("/goal")
async def goal_page(request: Request):
    user = current_user(request)
    if not user:
        return RedirectResponse("/login", status_code=303)
    return render(request, "goal.html", goals=GOALS, current=user["daily_goal"], mail_ready=mailer.configured(),
                  first=bool(request.query_params.get("first")))


@app.post("/goal")
async def goal_set(request: Request, goal: int = Form(...), first: str = Form(""), remind: str = Form("")):
    user = current_user(request)
    if not user:
        return RedirectResponse("/login", status_code=303)
    if goal in GOALS:
        db.set_daily_goal(user["id"], goal)
    if first and mailer.configured():
        db.set_reminders(user["id"], bool(remind))
    if first and user["level"]:
        return RedirectResponse("/day/1/q/1?welcome=1", status_code=303)
    return RedirectResponse("/dashboard", status_code=303)


@app.get("/dashboard")
async def dashboard(request: Request):
    user = current_user(request)
    if not user:
        return RedirectResponse("/login", status_code=303)
    level = user["level"]
    if level not in content.CONTENT:
        return RedirectResponse("/level", status_code=303)
    solved, attempted, solved_set = {}, {}, set()
    for row in db.get_progress(user["id"], level):
        if row["status"] == "solved":
            solved_set.add((row["day"], row["qnum"]))
            solved[row["day"]] = solved.get(row["day"], 0) + 1
        elif row["status"] == "attempted":
            attempted[row["day"]] = attempted.get(row["day"], 0) + 1
    days = []
    for n, day in sorted(content.CONTENT[level]["days"].items()):
        s, a = solved.get(n, 0), attempted.get(n, 0)
        state = "done" if s == 5 else "partial" if (s or a) else "todo"
        days.append({"n": n, "title": day["title"], "solved": s, "attempted": a, "state": state})
    total = sum(d["solved"] for d in days)
    cookie_tz = request.cookies.get("tz", "")
    if cookie_tz and cookie_tz != user["tz"] and stats._load_zone(cookie_tz):
        db.set_tz(user["id"], cookie_tz)
    active_dates = db.get_active_dates(user["id"])
    streak = stats.streak_v2(active_dates)
    repair = None
    if not db.has_event(user["id"], "streak_repair", stats.today_str()[:7]):
        repair = _repair_streak_length(active_dates)
    return render(
        request,
        "dashboard.html",
        days=days,
        next_q=_next_unsolved(user["level"], solved_set),
        solved_today=db.count_solved_since(user["id"], stats.today_start_epoch()),
        heatmap=_heatmap(db.get_solved_epochs(user["id"]), set(active_dates), set(map(str, streak["frozen"]))),
        repair=repair,
        review_due=_review_due_count(user),
        daily_goal=user["daily_goal"],
        started=bool(solved_set or attempted),
        level_label=content.CONTENT[level]["label"],
        total_solved=total,
        streak=streak,
    )


def _next_unsolved(level: str, solved_set: set):
    """(day, q) of the first question in plan order that is not solved yet, or None when all are."""
    for n, day in sorted(content.CONTENT[level]["days"].items()):
        for q in sorted(day["questions"]):
            if (n, q) not in solved_set:
                return {"day": n, "q": q}
    return None


def _repair_streak_length(active_dates: list):
    """The streak length a repair would restore, or None when repairing makes no sense: yesterday was
    missed AND the day before was a practice day, so there really was a streak to save."""
    today = stats.today()
    yesterday, before = (today - timedelta(days=1)).isoformat(), (today - timedelta(days=2)).isoformat()
    if yesterday in active_dates or before not in active_dates:
        return None
    gained = stats.streak_v2(active_dates + [yesterday])["current"]
    return gained if gained > stats.streak_v2(active_dates)["current"] else None


def _kind_of(level: str, day: int, q: int) -> str:
    _, question = _get_question(level, day, q)
    return question["kind"] if question else "code"


@app.get("/profile")
async def profile(request: Request):
    user = current_user(request)
    if not user:
        return RedirectResponse("/login", status_code=303)
    streak = stats.streak_v2(db.get_active_dates(user["id"]))
    result = badges.compute(
        db.get_all_progress(user["id"]), _kind_of,
        {lv: len(info["days"]) for lv, info in content.CONTENT.items()},
        streak["longest"], db.has_event(user["id"], "first_run", ""),
    )
    return render(request, "profile.html", streak=streak, **result)


def _review_due_count(user) -> int:
    _sweep_abandoned(user)
    return db.count_due_reviews(user["id"])


HEAT_WEEKS = 12


def _heatmap(solved_epochs, active: set, frozen: set):
    """Weeks (Monday first) ending with the current week. Level 0 none, 1 practiced, 2-4 by questions solved."""
    per_day = {}
    zone = stats.zone()
    for t in solved_epochs:
        d = datetime.fromtimestamp(t, zone).date().isoformat()
        per_day[d] = per_day.get(d, 0) + 1
    today = stats.today()
    start = today - timedelta(days=today.weekday() + 7 * (HEAT_WEEKS - 1))
    weeks = []
    for w in range(HEAT_WEEKS):
        col = []
        for i in range(7):
            d = start + timedelta(days=7 * w + i)
            ds = d.isoformat()
            if d > today:
                col.append(None)
                continue
            n = per_day.get(ds, 0)
            level = 0 if ds not in active and not n else 1 if not n else 2 if n < 3 else 3 if n < 5 else 4
            col.append({"date": ds, "n": n, "level": level, "frozen": ds in frozen})
        weeks.append(col)
    return weeks


def _get_question(level: str, day: int, q: int):
    d = content.CONTENT.get(level, {}).get("days", {}).get(day)
    if not d:
        return None, None
    return d, d["questions"].get(q)


@app.get("/day/{day}")
async def day_page(request: Request, day: int):
    user = current_user(request)
    if not user:
        return RedirectResponse("/login", status_code=303)
    d, _ = _get_question(user["level"], day, 1)
    if not d:
        return RedirectResponse("/dashboard", status_code=303)
    status = {r["qnum"]: r["status"] for r in db.get_progress(user["id"], user["level"]) if r["day"] == day}
    return render(
        request, "day.html", day=d, status=status, level=user["level"],
        last_day=max(content.CONTENT[user["level"]]["days"]),
    )


@app.get("/day/{day}/q/{q}")
async def question_page(request: Request, day: int, q: int):
    user = current_user(request)
    if not user:
        return RedirectResponse("/login", status_code=303)
    d, question = _get_question(user["level"], day, q)
    if not question:
        return RedirectResponse("/dashboard", status_code=303)
    saved = db.get_question_progress(user["id"], user["level"], day, q)
    return render(
        request,
        "question.html",
        day=d,
        question=question,
        saved_code=saved["code"] if saved and saved["code"] else question["starter"],
        status=saved["status"] if saved else "todo",
        level=user["level"],
        welcome=bool(request.query_params.get("welcome")),
        in_review=bool(request.query_params.get("review")) and db.get_review(user["id"], user["level"], day, q) is not None,
    )


@app.post("/api/run")
async def api_run(request: Request):
    user = current_user(request)
    if not user:
        return JSONResponse({"error": "Please log in."}, status_code=401)
    if not _throttle(user["id"], 1.5):
        return JSONResponse({"stage": "busy", "ok": False, "error": "Slow down a little."}, status_code=429)
    if (limited := _run_limited(request)):
        return limited
    body = await request.json()
    code = str(body.get("code", ""))
    stdin_text = str(body.get("stdin", ""))
    db.touch_activity(user["id"], stats.today_str())
    _log_run(user["id"])
    return JSONResponse(_with_help(await run_java(code, stdin_text)))


def _with_help(res: dict) -> dict:
    """Attach plain-language explanations to a compile failure."""
    if res.get("stage") == "compile" and res.get("error"):
        res["help"] = hints.explain(res["error"])
    return res


def _log_run(user_id: int) -> None:
    db.log_event(user_id, "first_run", once=True)
    db.log_event(user_id, "run")


REVIEW_ATTEMPTS = 4      # solved only after this many attempts -> worth reviewing
ABANDONED_AFTER = 3 * 86400  # attempted but untouched this long -> comes back for review


def _queue_once(user, level: str, day: int, q: int) -> None:
    """Queue a question for review the first time only, so graduating from the queue is final."""
    if db.log_event(user["id"], "review_added", f"{level}:{day}:{q}", once=True):
        db.enqueue_review(user["id"], level, day, q)


def _maybe_queue_review(user, day: int, q: int) -> None:
    row = db.get_question_progress(user["id"], user["level"], day, q)
    if row and row["attempts"] >= REVIEW_ATTEMPTS:
        _queue_once(user, user["level"], day, q)


def _sweep_abandoned(user) -> None:
    for r in db.abandoned_questions(user["id"], int(time.time()) - ABANDONED_AFTER):
        _queue_once(user, r["level"], r["day"], r["qnum"])


def _solve_feedback(user, day: int, q: int):
    """Called after a question becomes solved. Returns what to celebrate (or None): the day just
    became complete, and/or today's goal was just reached. Each fires once (via the events table)."""
    fb = {}
    _maybe_queue_review(user, day, q)
    if db.count_solved_in_day(user["id"], user["level"], day) >= 5 and db.log_event(
            user["id"], "day_complete", f"{user['level']}:{day}", once=True):
        nxt = content.CONTENT[user["level"]]["days"].get(day + 1)
        fb["day_complete"] = {"day": day, "next": {"n": day + 1, "title": nxt["title"]} if nxt else None}
    solved_today = db.count_solved_since(user["id"], stats.today_start_epoch())
    if solved_today >= user["daily_goal"] and db.log_event(
            user["id"], "goal_reached", stats.today_str(), once=True):
        fb["goal_reached"] = {"goal": user["daily_goal"], "solved_today": solved_today}
    return fb or None


def _throttle(user_id, gap: float) -> bool:
    """Simple per-user rate limit. Returns False when the call came too soon after the last one."""
    now = time.monotonic()
    if now - _last_run.get(user_id, 0) < gap:
        return False
    _last_run[user_id] = now
    return True


@app.post("/api/test")
async def api_test(request: Request):
    """Run the learner's code against the sample cases printed in the question."""
    user = current_user(request)
    if not user:
        return JSONResponse({"error": "Please log in."}, status_code=401)
    if not _throttle(user["id"], 3):
        return JSONResponse({"stage": "busy", "ok": False, "error": "Slow down a little."}, status_code=429)
    if (limited := _run_limited(request)):
        return limited
    body = await request.json()
    try:
        day, q = int(body["day"]), int(body["q"])
    except (KeyError, ValueError, TypeError):
        return JSONResponse({"error": "Bad request."}, status_code=400)
    _, question = _get_question(user["level"], day, q)
    if not question or not question["check_count"]:
        return JSONResponse({"error": "This question has no checks to run."}, status_code=404)
    code = str(body.get("code", ""))[:50_000]
    cases = [(s, False) for s in question["samples"]] + [(h, True) for h in question["hidden"]]
    res = await run_java_batch(code, [c["input"] for c, _ in cases])
    if res.get("stage") != "tests":
        return JSONResponse(_with_help(res))
    items = []
    for n, ((case, hidden), r) in enumerate(zip(cases, res["results"]), start=1):
        item = {
            "n": n,
            "hidden": hidden,
            "pass": bool(r["ok"] and grading.same(r["stdout"], case["output"])),
            "stderr": r["stderr"],
            "timed_out": r["timed_out"],
        }
        if not hidden:  # hidden cases never reveal their input or expected output
            item.update({"input": case["input"], "expected": case["output"], "actual": r["stdout"]})
        items.append(item)
    all_passed = all(i["pass"] for i in items)
    db.save_progress(user["id"], user["level"], day, q, code, "solved" if all_passed else None, True)
    db.touch_activity(user["id"], stats.today_str())
    _log_run(user["id"])
    if all_passed:
        db.log_event(user["id"], "samples_pass", f"{user['level']}:{day}:{q}", once=True)
    return {"stage": "tests", "results": items, "all_passed": all_passed,
            "celebrate": _solve_feedback(user, day, q) if all_passed else None}


_predict_cache = {}


async def _expected_output(level: str, day: int, q: int, question: dict):
    """The real output of a predict-the-output snippet, computed once by running it on the server."""
    key = (level, day, q)
    if key in _predict_cache:
        return _predict_cache[key]
    res = await run_java(question["starter"], "")
    if res.get("stage") == "run" and res.get("ok"):
        _predict_cache[key] = res["stdout"]
        return res["stdout"]
    return None


@app.post("/api/predict")
async def api_predict(request: Request):
    user = current_user(request)
    if not user:
        return JSONResponse({"error": "Please log in."}, status_code=401)
    body = await request.json()
    try:
        day, q = int(body["day"]), int(body["q"])
    except (KeyError, ValueError, TypeError):
        return JSONResponse({"error": "Bad request."}, status_code=400)
    _, question = _get_question(user["level"], day, q)
    if not question or question["kind"] != "predict":
        return JSONResponse({"error": "Not a predict-the-output question."}, status_code=404)
    expected = await _expected_output(user["level"], day, q, question)
    if expected is None:
        return JSONResponse({"error": "Could not evaluate this snippet right now. Try again."}, status_code=503)
    db.touch_activity(user["id"], stats.today_str())
    if body.get("reveal"):
        db.set_status(user["id"], user["level"], day, q, "attempted")
        _queue_once(user, user["level"], day, q)
        return {"revealed": True, "expected": expected}
    correct = grading.same(str(body.get("answer", ""))[:5000], expected)
    db.set_status(user["id"], user["level"], day, q, "solved" if correct else "attempted")
    return {"correct": correct, "celebrate": _solve_feedback(user, day, q) if correct else None}


@app.post("/api/save")
async def api_save(request: Request):
    user = current_user(request)
    if not user:
        return JSONResponse({"error": "Please log in."}, status_code=401)
    body = await request.json()
    try:
        day, q = int(body["day"]), int(body["q"])
    except (KeyError, ValueError, TypeError):
        return JSONResponse({"error": "Bad request."}, status_code=400)
    _, question = _get_question(user["level"], day, q)
    if not question:
        return JSONResponse({"error": "Unknown question."}, status_code=404)
    status = body.get("status")
    if status is not None and status not in STATUSES:
        return JSONResponse({"error": "Bad status."}, status_code=400)
    code = str(body.get("code", ""))[:50_000]
    db.save_progress(user["id"], user["level"], day, q, code, status, bool(body.get("ran")))
    db.touch_activity(user["id"], stats.today_str())
    return {"ok": True, "celebrate": _solve_feedback(user, day, q) if status == "solved" else None}


@app.post("/api/report")
async def api_report(request: Request):
    """Learner flags a problem with a question; the code they had in the editor is attached."""
    user = current_user(request)
    if not user:
        return JSONResponse({"error": "Please log in."}, status_code=401)
    if not _throttle(f"report:{user['id']}", 10):
        return JSONResponse({"error": "Please wait a few seconds before sending another report."}, status_code=429)
    body = await request.json()
    try:
        day, q = int(body["day"]), int(body["q"])
    except (KeyError, ValueError, TypeError):
        return JSONResponse({"error": "Bad request."}, status_code=400)
    _, question = _get_question(user["level"], day, q)
    category = body.get("category")
    if not question or category not in REPORT_CATEGORIES:
        return JSONResponse({"error": "Bad request."}, status_code=400)
    db.add_report(user["id"], user["level"], day, q, category,
                  str(body.get("note", ""))[:2000], str(body.get("code", ""))[:50_000])
    return {"ok": True}


@app.get("/admin")
async def admin_page(request: Request):
    user = current_user(request)
    if not user:
        return RedirectResponse("/login", status_code=303)
    if not is_admin(user):
        return JSONResponse({"error": "Not found."}, status_code=404)
    today = stats.today_str()
    week_start = (date.fromisoformat(today) - timedelta(days=6)).isoformat()
    return render(
        request, "admin.html",
        overview=db.admin_overview(today, week_start),
        reports=db.list_reports(),
        hardest=db.hardest_questions(),
        sandbox_status=sandbox.STATUS,
    )


@app.post("/admin/reports/{report_id}/resolve")
async def admin_resolve(request: Request, report_id: int):
    user = current_user(request)
    if not is_admin(user):
        return JSONResponse({"error": "Not found."}, status_code=404)
    db.resolve_report(report_id)
    return RedirectResponse("/admin", status_code=303)


def _review_item(row) -> dict:
    d, question = _get_question(row["level"], row["day"], row["qnum"])
    return {"level": row["level"], "day": row["day"], "q": row["qnum"], "stage": row["stage"], "due_at": row["due_at"],
            "title": d["title"] if d else "", "category": question["category"] if question else ""}


@app.get("/review")
async def review_page(request: Request):
    user = current_user(request)
    if not user:
        return RedirectResponse("/login", status_code=303)
    _sweep_abandoned(user)
    due = [_review_item(r) for r in db.due_reviews(user["id"]) if r["level"] == user["level"]]
    later = [_review_item(r) for r in db.upcoming_reviews(user["id"]) if r["level"] == user["level"]]
    return render(request, "review.html", due=due, later=later, now=int(time.time()))


@app.post("/api/review/rate")
async def api_review_rate(request: Request):
    user = current_user(request)
    if not user:
        return JSONResponse({"error": "Please log in."}, status_code=401)
    body = await request.json()
    try:
        day, q = int(body["day"]), int(body["q"])
    except (KeyError, ValueError, TypeError):
        return JSONResponse({"error": "Bad request."}, status_code=400)
    result = db.rate_review(user["id"], user["level"], day, q, str(body.get("rating", "")))
    if result is None:
        return JSONResponse({"error": "That question is not in your review queue."}, status_code=404)
    db.touch_activity(user["id"], stats.today_str())
    db.log_event(user["id"], "review_done", f"{user['level']}:{day}:{q}:{body.get('rating')}")
    nxt = [r for r in db.due_reviews(user["id"]) if r["level"] == user["level"]]
    return {"ok": True, "result": result,
            "next": f"/day/{nxt[0]['day']}/q/{nxt[0]['qnum']}?review=1" if nxt else "/review"}
