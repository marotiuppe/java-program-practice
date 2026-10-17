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
from starlette.middleware.gzip import GZipMiddleware
from fastapi.responses import FileResponse, JSONResponse, RedirectResponse, Response
from fastapi.staticfiles import StaticFiles
from fastapi.templating import Jinja2Templates
from starlette.middleware.sessions import SessionMiddleware

from . import admin, badges, content, db, email_templates, grading, hints, mailer, ratelimit, reminders, sandbox, sharecard, stats
from .runner import run_java, run_java_batch, sandbox_check_started, sandbox_self_test

logging.basicConfig(level=logging.INFO)
log = logging.getLogger("app")
HERE = os.path.dirname(__file__)
templates = Jinja2Templates(directory=os.path.join(HERE, "templates"))
_asset_hash = {}


def static_url(path: str) -> str:
    """/static/<path>?v=<content hash>: changes whenever the file changes, so it can be cached forever."""
    v = _asset_hash.get(path)
    if v is None:
        try:
            with open(os.path.join(HERE, "static", path), "rb") as f:
                v = hashlib.sha1(f.read()).hexdigest()[:10]
        except OSError:
            v = "0"
        _asset_hash[path] = v
    return f"/static/{path}?v={v}"


templates.env.globals["static"] = static_url
templates.env.filters["ts"] = lambda t: datetime.fromtimestamp(t, stats.zone()).strftime("%Y-%m-%d %H:%M") if t else ""

EMAIL_RE = re.compile(r"^[^@\s]+@[^@\s]+\.[^@\s]+$")
HANDLE_BAD = re.compile(r"[^A-Za-z0-9._-]")
REF_RE = re.compile(r"^[a-z0-9]{6,12}$")
REFERRAL_CAP = 20  # freezes one learner can earn by inviting; stops farming with throwaway accounts


def clean_handle(text: str) -> str:
    """A username safe to print on a card: letters, digits, dot, dash, underscore; no leading @ or symbol; 24 chars."""
    text = HANDLE_BAD.sub("", (text or "").strip().lstrip("@"))
    return text.lstrip("._-")[:24]


def card_handle(user) -> str:
    """"@name" shown on shareable cards: the username the learner chose, else the part of their email before
    the @ (marotiuppe@gmail.com -> @marotiuppe). Empty when they turned names off."""
    if user["card_name_off"]:
        return ""
    name = clean_handle(user["display_name"]) or clean_handle(user["email"].split("@")[0].split("+")[0])
    return "@" + name if name else "" 
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
    # in the background: the web server starts answering at once; code runs wait until it finishes
    sandbox_check_started.set()
    selftest = asyncio.create_task(asyncio.to_thread(sandbox_self_test))
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
    selftest.cancel()


app = FastAPI(lifespan=lifespan)
app.add_middleware(
    SessionMiddleware,
    secret_key=os.environ.get("SECRET_KEY", "dev-only-change-me"),
    same_site="lax",
    https_only=bool(os.environ.get("RENDER")),
    max_age=60 * 60 * 24 * 30,
)
app.include_router(admin.router)
class CachedStatic(StaticFiles):
    """Static files with browser caching: URLs that carry ?v=<hash> (see static_url) never change, so they are
    cached for a year; anything else for an hour."""

    async def get_response(self, path, scope):
        resp = await super().get_response(path, scope)
        if resp.status_code == 200:
            resp.headers["Cache-Control"] = ("public, max-age=31536000, immutable" if scope.get("query_string")
                                             else "public, max-age=3600")
        return resp


app.mount("/static", CachedStatic(directory=os.path.join(HERE, "static")), name="static")
app.add_middleware(GZipMiddleware, minimum_size=500)  # added last = outermost: compresses every response


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
    response = await call_next(request)
    code = request.query_params.get("ref", "").lower()
    if REF_RE.match(code):  # someone arrived through an invite link: remember who until they sign up
        response.set_cookie("ref", code, max_age=30 * 86400, samesite="lax", httponly=True,
                            secure=bool(os.environ.get("RENDER")))
    return response


_UNSET = object()


def current_user(request: Request):
    """The logged-in user row, looked up once per request (handlers and render() both ask)."""
    cached = getattr(request.state, "user_cache", _UNSET)
    if cached is not _UNSET:
        return cached
    request.state.user_cache = _load_user(request)
    return request.state.user_cache


def forget_user(request: Request) -> None:
    """Drop the per-request copy after changing the user or the session, so the next lookup is fresh."""
    request.state.user_cache = _UNSET


def _load_user(request: Request):
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


def _send_email(kind: str, to: str, base: str, headers: dict | None = None, **ctx) -> None:
    subject, text, html = email_templates.render(kind, base, **ctx)
    mailer.send(to, subject, text, headers=headers, html=html)


def _send_verification(email: str, link: str, base: str) -> None:
    _send_email("verify", email, base, link=link)


def _send_reset(email: str, link: str, base: str) -> None:
    _send_email("reset", email, base, link=link)


def _send_password_changed(email: str, base: str) -> None:
    _send_email("changed", email, base)


def is_admin(user) -> bool:
    """Admin = an account whose email is listed in ADMIN_EMAILS AND whose email address is confirmed,
    so nobody can gain access just by registering an admin address they do not own."""
    return bool(user) and bool(user["email_verified"]) and user["email"].lower() in ADMIN_EMAILS


PUBLIC_COUNT_MIN = int(os.environ.get("PUBLIC_COUNT_MIN", "10"))  # show site totals only once they are not embarrassing
_counts_cache = {"t": None, "v": None}


def public_counts():
    """{"learners": "1,240", "solved": "38,000"} for the login and signup pages, refreshed every 5 minutes,
    or None while there are fewer than PUBLIC_COUNT_MIN learners."""
    now = time.monotonic()
    if _counts_cache["t"] is None or now - _counts_cache["t"] > 300:
        try:
            users, solved = db.public_counts()
            _counts_cache["v"] = ({"learners": f"{users:,}", "solved": f"{solved:,}"} if users >= PUBLIC_COUNT_MIN else None)
        except Exception:
            _counts_cache["v"] = None
        _counts_cache["t"] = now
    return _counts_cache["v"]


_news_cache = {"mtime": None, "items": []}


def whats_new(limit: int = 6) -> list:
    """Entries of static/whats-new.json (newest first), re-read when the file changes."""
    path = os.path.join(HERE, "static", "whats-new.json")
    try:
        mtime = os.path.getmtime(path)
        if _news_cache["mtime"] != mtime:
            _news_cache["items"] = [n for n in json.load(open(path, encoding="utf-8"))
                                    if isinstance(n, dict) and n.get("title") and n.get("text")]
            _news_cache["mtime"] = mtime
    except (OSError, ValueError):
        return []
    return _news_cache["items"][:limit]


def _inviter_from(request: Request):
    """The learner whose invite link brought this visitor (?ref= or the cookie), or None."""
    code = (request.query_params.get("ref") or request.cookies.get("ref") or "").lower()
    return db.user_by_ref_code(code) if REF_RE.match(code) else None


def render(request: Request, name: str, status_code: int = 200, **ctx):
    if name == "auth.html":
        inviter = _inviter_from(request)
        ctx.setdefault("invited_by", (card_handle(inviter) or "A friend") if inviter else None)  # visitors who are not logged in see the weekly top 10 and what is new
        ctx.setdefault("news", whats_new())
        ctx.setdefault("counts", public_counts())
        ctx.setdefault("board_top", [{"h": r["handle"], "n": r["n"]}
                                      for r in _board_rows("", stats.week_start_epoch())[:10]])
    return templates.TemplateResponse(
        request, name,
        {"user": (u := current_user(request)), "is_admin": is_admin(u), "site_url": base_url(request), **ctx},
        status_code=status_code)


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


LEVEL_BLURBS = {
    "beginner": "Syntax, loops, strings, arrays and basic OOP. No experience needed.",
    "intermediate": "OOP in depth, collections, algorithms, Java 17 features and streams.",
    "advanced": "Data structures, graphs, dynamic programming, concurrency and design.",
}
QUESTION_KINDS = [
    ("Real-world scenarios", "Small programs that mirror jobs like billing, grading and inventory."),
    ("Interview questions", "The classics that come up in screening rounds."),
    ("Assessment style", "Company-style problems with samples and edge cases."),
    ("Predict the output", "Trace code in your head, then check your answer."),
    ("Debug the code", "Find and fix what is broken, the way you do at work."),
]


@app.get("/")
async def index(request: Request):
    user = current_user(request)
    if not user:
        levels = [(k, v["label"], LEVEL_BLURBS[k]) for k, v in content.CONTENT.items()]
        total = sum(len(d["questions"]) for v in content.CONTENT.values() for d in v["days"].values())
        return render(request, "landing.html", levels=levels, total=total, kinds=QUESTION_KINDS)
    if not user["level"]:
        return RedirectResponse("/level", status_code=303)
    return RedirectResponse("/dashboard", status_code=303)


@app.get("/plan/{level}")
async def plan_page(request: Request, level: str):
    info = content.CONTENT.get(level)
    if not info:
        return JSONResponse({"error": "Not found."}, status_code=404)
    days = [{"n": n, "title": d["title"]} for n, d in sorted(info["days"].items())]
    return render(request, "plan.html", level=level, label=info["label"], blurb=LEVEL_BLURBS[level], days=days)


@app.get("/plan/{level}/day/{n}")
async def plan_day_page(request: Request, level: str, n: int):
    d = content.CONTENT.get(level, {}).get("days", {}).get(n)
    if not d:
        return JSONResponse({"error": "Not found."}, status_code=404)
    cats = [q["category"] for _, q in sorted(d["questions"].items())]
    last = max(content.CONTENT[level]["days"])
    return render(request, "plan_day.html", level=level, label=content.CONTENT[level]["label"], day=d,
                  categories=cats, prev_n=n - 1 if n > 1 else None, next_n=n + 1 if n < last else None)


@app.get("/robots.txt", include_in_schema=False)
async def robots(request: Request):
    body = ("User-agent: *\nAllow: /\nDisallow: /api/\nDisallow: /admin\nDisallow: /account\nDisallow: /day/\n"
            "Disallow: /dashboard\nDisallow: /review\nDisallow: /profile\nDisallow: /reset/\nDisallow: /verify/\n"
            f"Sitemap: {base_url(request)}/sitemap.xml\n")
    return Response(body, media_type="text/plain")


@app.get("/sitemap.xml", include_in_schema=False)
async def sitemap(request: Request):
    root = base_url(request)
    urls = [f"{root}/", f"{root}/privacy", f"{root}/signup"]
    for level, info in content.CONTENT.items():
        urls.append(f"{root}/plan/{level}")
        urls += [f"{root}/plan/{level}/day/{n}" for n in sorted(info["days"])]
    xml = ('<?xml version="1.0" encoding="UTF-8"?>\n<urlset xmlns="http://www.sitemaps.org/schemas/sitemap/0.9">\n'
           + "".join(f"  <url><loc>{u}</loc></url>\n" for u in urls) + "</urlset>\n")
    return Response(xml, media_type="application/xml")


@app.get("/signup")
async def signup_page(request: Request):
    return render(request, "auth.html", mode="signup", error=None)


@app.post("/signup")
async def signup(request: Request, background: BackgroundTasks, email: str = Form(...), password: str = Form(...),
                 username: str = Form("")):
    email = email.strip().lower()
    wait = ratelimit.hit(f"signup:{client_ip(request)}", *SIGNUP_IP)
    if wait:
        return render(request, "auth.html", status_code=429, mode="signup",
                      error=f"Too many sign-ups from your network. Try again in {ratelimit.human(wait)}.")
    if not EMAIL_RE.match(email):
        return render(request, "auth.html", mode="signup", error="Enter a valid email address.")
    if len(password) < 8:
        return render(request, "auth.html", mode="signup", error="Password must be at least 8 characters.")
    name = clean_handle(username)
    if username.strip() and not name:
        return render(request, "auth.html", mode="signup",
                      error="A username can use letters, numbers, dots, dashes and underscores.")
    inviter = _inviter_from(request)
    uid = db.create_user(email, password, verified=not mailer.configured(), display_name=name,
                         referred_by=inviter["id"] if inviter else None)
    if uid is None:
        return render(request, "auth.html", mode="signup", error="That email is already registered.")
    _login_session(request, db.get_user(uid))
    db.log_event(uid, "signup")
    if inviter:
        db.log_event(uid, "referred", str(inviter["id"]))
    if mailer.configured():
        token = db.create_token(uid, "verify", VERIFY_TTL)
        background.add_task(_send_verification, email, f"{base_url(request)}/verify/{token}", base_url(request))
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
    return RedirectResponse("/admin" if is_admin(user) else "/", status_code=303)


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
            background.add_task(_send_reset, email, f"{base_url(request)}/reset/{token}", base_url(request))
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
    background.add_task(_send_password_changed, user["email"], base_url(request))
    return RedirectResponse("/login?reset=1", status_code=303)


@app.get("/verify/{token}")
async def verify(request: Request, token: str):
    uid = db.consume_token(token, "verify")
    if uid is None:
        return render(request, "message.html", status_code=400, title="Link expired",
                      text="This confirmation link is invalid, expired or already used. Log in and request a new one.",
                      href="/login", link="Log in")
    db.mark_verified(uid)
    forget_user(request)  # the page header must not still say "confirm your email"
    return render(request, "message.html", title="Email confirmed", text="Thanks, your email address is confirmed.",
                  href="/dashboard", link="Continue")


@app.post("/verify/resend")
async def verify_resend(request: Request, background: BackgroundTasks):
    user = current_user(request)
    if not user:
        return RedirectResponse("/login", status_code=303)
    if not user["email_verified"] and mailer.configured() and not ratelimit.hit(f"resend:{user['id']}", *RESEND_USER):
        token = db.create_token(user["id"], "verify", VERIFY_TTL)
        background.add_task(_send_verification, user["email"], f"{base_url(request)}/verify/{token}", base_url(request))
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
                  reminders_on=bool(user["reminders"]), hints_on=bool(user["hints_on"]), mail_ready=mailer.configured(), verified=bool(user["email_verified"]),
                  card_name=card_handle(user), name_value=user["display_name"], name_hidden=bool(user["card_name_off"]),
                  board_on=bool(user["leaderboard"]))


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
        return render(request, "account.html", status_code=429, saved=False, reminders_on=bool(user["reminders"]), hints_on=bool(user["hints_on"]),
                      mail_ready=mailer.configured(), verified=bool(user["email_verified"]),
                      error=f"Too many wrong passwords. Try again in {ratelimit.human(wait)}.")
    if not db.verify_password(password, user["pw_hash"]):
        ratelimit.record(key)
        return render(request, "account.html", status_code=400, saved=False, reminders_on=bool(user["reminders"]), hints_on=bool(user["hints_on"]),
                      mail_ready=mailer.configured(), verified=bool(user["email_verified"]),
                      error="That password is not correct.")
    db.delete_user(user["id"])
    request.session.clear()
    forget_user(request)
    return render(request, "message.html", title="Account deleted",
                  text="Your account and all of its data have been permanently deleted.", href="/signup", link="Create a new account")


@app.post("/account/name")
async def account_name(request: Request, username: str = Form(""), hide: str = Form("")):
    user = current_user(request)
    if not user:
        return RedirectResponse("/login", status_code=303)
    name = clean_handle(username)
    if username.strip() and not name:
        return render(request, "account.html", status_code=400, error=None, name_error="Use letters, numbers, dots, dashes and underscores.",
                      saved=False, reminders_on=bool(user["reminders"]), hints_on=bool(user["hints_on"]),
                      mail_ready=mailer.configured(), verified=bool(user["email_verified"]),
                      card_name=card_handle(user), name_value=username, name_hidden=bool(user["card_name_off"]),
                      board_on=bool(user["leaderboard"]))
    db.set_display_name(user["id"], name, bool(hide))
    return RedirectResponse("/account?saved=1", status_code=303)


@app.post("/account/leaderboard")
async def account_leaderboard(request: Request, on: str = Form(""), back: str = Form("")):
    user = current_user(request)
    if not user:
        return RedirectResponse("/login", status_code=303)
    db.set_leaderboard(user["id"], bool(on))
    _board_cache.clear()
    dest = back if back in ("/leaderboard", "/dashboard") else "/account?saved=1"
    return RedirectResponse(dest, status_code=303)


@app.get("/join/{code}")
async def join_link(code: str):
    """Personal invite link: the ref cookie is set by the middleware on the page it redirects to."""
    code = code.lower()
    return RedirectResponse(f"/signup?ref={code}" if REF_RE.match(code) else "/signup", status_code=303)


@app.get("/invite")
def invite_page(request: Request):
    user = current_user(request)
    if not user:
        return RedirectResponse("/login", status_code=303)
    code = user["ref_code"] or db.ensure_ref_code(user["id"])
    streak = stats.streak_for(user, db.get_active_dates(user["id"]))
    return render(request, "invite.html", link=f"{base_url(request)}/join/{code}", stats=db.referral_stats(user["id"]),
                  freezes=streak["freezes"], cap=REFERRAL_CAP, handle=card_handle(user))


@app.get("/leaderboard")
def leaderboard_page(request: Request, period: str = "week", level: str = ""):
    user = current_user(request)
    if not user:
        return RedirectResponse("/login", status_code=303)
    period = "all" if period == "all" else "week"
    level = level if level in content.CONTENT else ""
    rows = _board_rows(level, stats.week_start_epoch() if period == "week" else 0)
    ranked = [{"rank": i + 1, "handle": r["handle"], "n": r["n"], "me": r["uid"] == user["id"]} for i, r in enumerate(rows)]
    me = next((r for r in ranked if r["me"]), None)
    return render(request, "leaderboard.html", top=ranked[:10], me=me if me and me["rank"] > 10 else None,
                  me_in_top=bool(me and me["rank"] <= 10), period=period, level=level,
                  levels=[(k, v["label"]) for k, v in content.CONTENT.items()], on=bool(user["leaderboard"]),
                  my_handle=card_handle(user) or "Anonymous learner", total=len(ranked))


@app.post("/account/hints")
async def account_hints(request: Request, on: str = Form(""), back: str = Form("")):
    user = current_user(request)
    if not user:
        return RedirectResponse("/login", status_code=303)
    db.set_hints_on(user["id"], bool(on))
    # a question page can send the learner straight back to where they were (same-site paths only)
    dest = back if back.startswith("/day/") else "/account?saved=1"
    return RedirectResponse(dest, status_code=303)


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
    if _repair_streak_length(user, active) and db.log_event(
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
    rows = db.get_progress_rows(user["id"], level)
    for row in rows:
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
    streak = stats.streak_for(user, active_dates)
    repair = _repair_streak_length(user, active_dates)  # pure calculation; only then check the monthly limit
    if repair and db.has_event(user["id"], "streak_repair", stats.today_str()[:7]):
        repair = None
    solved_epochs = db.get_solved_epochs(user["id"])
    day_start = stats.today_start_epoch()
    board = _board_summary(user)
    return render(
        request,
        "dashboard.html",
        days=days,
        next_q=_next_unsolved(user["level"], solved_set),
        solved_today=sum(1 for t in solved_epochs if t >= day_start),
        heatmap=_heatmap(solved_epochs, set(active_dates), set(map(str, streak["frozen"]))),
        repair=repair,
        review_due=_review_due_from_rows(user, rows),
        daily_goal=user["daily_goal"],
        started=bool(solved_set or attempted),
        level_label=content.CONTENT[level]["label"],
        total_solved=total,
        streak=streak,
        board=board,
        snapshot={"k": _snap_key(user), "level": content.CONTENT[level]["label"], "streak": streak["current"],
                  "board": board,
                  "longest": streak["longest"], "solved": total, "next": _next_unsolved(level, solved_set),
                  "solved_today": sum(1 for t in solved_epochs if t >= day_start), "goal": user["daily_goal"]},
    )


def _next_unsolved(level: str, solved_set: set):
    """(day, q) of the first question in plan order that is not solved yet, or None when all are."""
    for n, day in sorted(content.CONTENT[level]["days"].items()):
        for q in sorted(day["questions"]):
            if (n, q) not in solved_set:
                return {"day": n, "q": q}
    return None


def _repair_streak_length(user, active_dates: list):
    """The streak length a repair would restore, or None when repairing makes no sense: yesterday was
    missed AND the day before was a practice day, so there really was a streak to save."""
    today = stats.today()
    yesterday, before = (today - timedelta(days=1)).isoformat(), (today - timedelta(days=2)).isoformat()
    if yesterday in active_dates or before not in active_dates:
        return None
    gained = stats.streak_for(user, active_dates + [yesterday])["current"]
    return gained if gained > stats.streak_for(user, active_dates)["current"] else None


def _kind_of(level: str, day: int, q: int) -> str:
    _, question = _get_question(level, day, q)
    return question["kind"] if question else "code"


@app.get("/profile")
async def profile(request: Request):
    user = current_user(request)
    if not user:
        return RedirectResponse("/login", status_code=303)
    streak = stats.streak_for(user, db.get_active_dates(user["id"]))
    result = badges.compute(
        db.get_all_progress(user["id"]), _kind_of,
        {lv: len(info["days"]) for lv, info in content.CONTENT.items()},
        streak["longest"], db.has_event(user["id"], "first_run", ""),
    )
    return render(request, "profile.html", streak=streak, **result, snapshot={
        "k": _snap_key(user), "xp": result["learner"]["xp"], "learner": result["learner"]["level"],
        "badges": sum(1 for b in result["badges"] if b["earned"])})


def _review_due_from_rows(user, rows) -> int:
    """Dashboard version of the review count: finds abandoned questions in rows we already loaded."""
    cutoff = int(time.time()) - ABANDONED_AFTER
    cands = [r for r in rows if r["status"] == "attempted" and r["has_code"] and r["updated_at"] < cutoff]
    if cands:
        seen = db.event_metas(user["id"], "review_added")
        for r in cands:
            if f"{user['level']}:{r['day']}:{r['qnum']}" not in seen:
                _queue_once(user, user["level"], r["day"], r["qnum"])
    return db.count_due_reviews(user["id"])


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
    rows = [r for r in db.get_progress(user["id"], user["level"]) if r["day"] == day]
    status = {r["qnum"]: r["status"] for r in rows}
    hint_used = {r["qnum"] for r in rows if r["hint_level"]}
    return render(
        request, "day.html", day=d, status=status, hint_used=hint_used, level=user["level"],
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
        feedback=db.get_feedback(user["id"], user["level"], day, q),
        hints_on=bool(user["hints_on"]),
        hints_total=len(question["hints"]),
        hints_shown=question["hints"][: saved["hint_level"]] if saved else [],
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


STREAK_MILESTONES = (3, 7, 14, 30, 60, 100)
SHARE_KINDS = ("day", "goal", "streak", "level", "progress")
SHARE_CARD_USER = (60, 3600)  # card requests per learner per hour
SHARE_IMAGE_IP = (200, 600)   # image renders per IP per 10 minutes (previews are fetched by a few crawlers)


_board_cache = {}
BOARD_TTL = int(os.environ.get("BOARD_TTL", "60"))  # seconds; one shared query, so compute it at most once a minute


def _board_rows(level: str, since: int):
    """Ranked leaderboard rows [{uid, handle, n}], cached briefly."""
    key = (level, since)
    hit = _board_cache.get(key)
    if hit and time.monotonic() - hit[0] < BOARD_TTL:
        return hit[1]
    rows = [{"uid": r["id"], "n": int(r["n"]),
             "handle": "Anonymous learner" if r["card_name_off"] else (card_handle(r) or "Anonymous learner")}
            for r in db.leaderboard_rows(since, level)]
    if len(_board_cache) > 50:
        _board_cache.clear()
    _board_cache[key] = (time.monotonic(), rows)
    return rows


def _board_summary(user) -> dict:
    """This week's top 10 and where this learner stands (shown on the dashboard, saved for the waiting screen)."""
    rows = _board_rows("", stats.week_start_epoch())
    me = next(({"rank": i + 1, "n": r["n"]} for i, r in enumerate(rows) if r["uid"] == user["id"]), None)
    return {"top": [{"h": r["handle"], "n": r["n"]} for r in rows[:10]], "me": me, "on": bool(user["leaderboard"])}


def _snap_key(user) -> str:
    """Opaque per-account tag so a progress summary saved on this device is never mixed between accounts."""
    return hashlib.sha256(f"snapshot:{user['id']}".encode()).hexdigest()[:10]


def _level_days_done(user_id: int, level: str) -> int:
    """Days of this level where every question is solved."""
    solved = {}
    for lv, d, qn, status in db.get_all_progress(user_id):
        if lv == level and status == "solved":
            solved.setdefault(d, set()).add(qn)
    days = content.CONTENT[level]["days"]
    return sum(1 for n, day in days.items() if len(solved.get(n, ())) >= len(day["questions"]))


def _share_card(user, kind: str, day):
    """The card for one milestone, or None when the learner has not actually reached it. Built only from the
    learner's real progress and contains no personal details."""
    level = user["level"]
    lvl = content.CONTENT[level]
    total_days = len(lvl["days"])
    streak = stats.streak_for(user, db.get_active_dates(user["id"]))
    solved_total = db.count_solved_since(user["id"], 0)
    days_done = _level_days_done(user["id"], level)
    streak_tile = [[str(streak["current"]), "day streak"]] if streak["current"] else []
    solved_tile = [[str(solved_total), "question solved" if solved_total == 1 else "questions solved"]]
    progress = [days_done / total_days, f"{days_done} of {total_days} days finished"]
    if kind == "day":
        if not isinstance(day, int) or day not in lvl["days"] or db.count_solved_in_day(user["id"], level, day) < 5:
            return None
        return {"i": "check", "t": f"I completed Day {day}!", "s": f"{lvl['label']} · {lvl['days'][day]['title']}",
                "st": (streak_tile + solved_tile + [[f"{days_done}/{total_days}", "days done"]])[:3], "p": progress}
    if kind == "goal":
        today = db.count_solved_since(user["id"], stats.today_start_epoch())
        if today < 1:
            return None
        title = "I hit my daily goal!" if today >= user["daily_goal"] else f"I solved {today} today!"
        return {"i": "target", "t": title, "s": lvl["label"],
                "st": (streak_tile + [[str(today), "solved today"]] + solved_tile)[:3], "p": None}
    if kind == "streak":
        if streak["current"] < 1:
            return None
        return {"i": "flame", "t": f"I'm on a {streak['current']}-day streak!", "s": f"{lvl['label']} · Java, every day",
                "st": [[str(streak["longest"]), "longest streak"]] + solved_tile, "p": None}
    if kind == "level":
        if days_done < total_days:
            return None
        total_q = sum(len(d["questions"]) for d in lvl["days"].values())
        return {"i": "trophy", "t": f"I completed the {lvl['label']} level!", "s": f"All {total_q} questions solved",
                "st": (solved_tile + [[f"{total_days}", "days done"]] + streak_tile)[:3], "p": [1, f"{total_days} of {total_days} days finished"]}
    return {"i": "star", "t": "I'm learning Java!", "s": f"{lvl['label']} · 30-day challenge",
            "st": (streak_tile + solved_tile + [[f"{days_done}/{total_days}", "days done"]])[:3], "p": progress}


def _share_text(card: dict) -> str:
    stats_line = " · ".join(f"{v} {l}" for v, l in card["st"])
    return (f"{card['t']} {stats_line} on the free 30-day Java challenge. Think you can beat it? "
            "Join me: #Java #LearnToCode")


@app.post("/api/share/card")
async def api_share_card(request: Request):
    user = current_user(request)
    if not user:
        return JSONResponse({"error": "Log in first."}, status_code=401)
    if ratelimit.hit(f"sharecard:{user['id']}", *SHARE_CARD_USER):
        return JSONResponse({"error": "Too many cards. Try again later."}, status_code=429)
    try:
        body = await request.json()
    except Exception:
        body = {}
    kind = body.get("kind") if isinstance(body, dict) else None
    day = body.get("day") if isinstance(body, dict) else None
    if kind not in SHARE_KINDS:
        return JSONResponse({"error": "Unknown card."}, status_code=400)
    if user["level"] not in content.CONTENT:
        return JSONResponse({"error": "Choose a level first."}, status_code=400)
    card = _share_card(user, kind, day if isinstance(day, int) and not isinstance(day, bool) else None)
    if card is None:
        return JSONResponse({"error": "You have not reached that milestone yet."}, status_code=400)
    handle = card_handle(user)
    if handle:
        card["u"] = handle
    card["r"] = user["ref_code"] or db.ensure_ref_code(user["id"])  # lets the card's QR code and button invite people
    token = sharecard.make_token(card)
    return {"url": f"{base_url(request)}/s/{token}", "card_url": f"/s/{token}/card.png", "text": _share_text(card),
            "title": card["t"], "who": handle}


@app.get("/s/{token}")
async def share_page(request: Request, token: str):
    card = sharecard.read_token(token)
    if card is None:
        return render(request, "message.html", status_code=404, title="Card not found",
                      text="This share link is not valid.", href="/", link="Go to Java Practice")
    desc = f"{card.get('s', '')} · " + ", ".join(f"{v} {l}" for v, l in card["st"])
    resp = render(request, "share.html", card=card, token=token, desc=desc.strip(" ·,"),
                  challenge=sharecard.CHALLENGE.get(card["i"], "Join me!"))
    if card.get("r"):  # remember who shared this, in case the visitor signs up later
        resp.set_cookie("ref", card["r"], max_age=30 * 86400, samesite="lax", httponly=True,
                        secure=bool(os.environ.get("RENDER")))
    return resp


async def _share_image(request: Request, token: str, kind: str):
    if ratelimit.hit(f"shareimg:{client_ip(request)}", *SHARE_IMAGE_IP):
        return Response("Too many requests", status_code=429)
    png = await asyncio.to_thread(sharecard.render_cached, token, kind, base_url(request))
    if png is None:
        return Response("Not found", status_code=404)
    return Response(png, media_type="image/png", headers={"Cache-Control": "public, max-age=86400"})


@app.get("/s/{token}/card.png")
async def share_card_image(request: Request, token: str):
    return await _share_image(request, token, "card")


@app.get("/s/{token}/og.png")
async def share_og_image(request: Request, token: str):
    return await _share_image(request, token, "og")


def _referral_reward(user):
    """First solved question of an invited learner: both get a streak freeze (once). Returns what to celebrate."""
    if not db.log_event(user["id"], "referral_reward", str(user["referred_by"]), once=True):
        return None
    today_iso = stats.today_str()
    db.add_freeze_bonus(user["id"], today_iso)
    inviter = db.get_user(user["referred_by"])
    if inviter and db.count_events(inviter["id"], "referral_earned") < REFERRAL_CAP:
        db.add_freeze_bonus(inviter["id"], today_iso)
        db.log_event(inviter["id"], "referral_earned", str(user["id"]))
    return {"freeze": True, "inviter": (card_handle(inviter) or "your friend") if inviter else "your friend"}


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
    if user["referred_by"]:  # a once-only flag inside makes this a single cheap lookup after the first solve
        reward = _referral_reward(user)
        if reward:
            fb["referral"] = reward
    if "day_complete" in fb and _level_days_done(user["id"], user["level"]) >= len(content.CONTENT[user["level"]]["days"]) \
            and db.log_event(user["id"], "level_complete", user["level"], once=True):
        fb["level_complete"] = {"level": content.CONTENT[user["level"]]["label"]}
    streak_now = stats.streak_for(user, db.get_active_dates(user["id"]))["current"]
    if streak_now in STREAK_MILESTONES and db.log_event(user["id"], "streak_milestone", str(streak_now), once=True):
        fb["streak_milestone"] = {"days": streak_now}
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
    if question.get("expected") is not None:  # computed ahead of time: no Java run needed
        return question["expected"]
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


@app.post("/api/feedback")
async def api_feedback(request: Request):
    """Thumbs up/down and a 'too easy / about right / too hard' vote for one question (one row per learner)."""
    user = current_user(request)
    if not user:
        return JSONResponse({"error": "Please log in."}, status_code=401)
    if not _throttle(f"feedback:{user['id']}", 0.4):
        return JSONResponse({"error": "Slow down a little."}, status_code=429)
    body = await request.json()
    try:
        day, q = int(body["day"]), int(body["q"])
    except (KeyError, ValueError, TypeError):
        return JSONResponse({"error": "Bad request."}, status_code=400)
    _, question = _get_question(user["level"], day, q)
    if not question:
        return JSONResponse({"error": "Unknown question."}, status_code=404)
    rating, difficulty = body.get("rating"), body.get("difficulty")
    if rating is not None and rating not in (-1, 0, 1):
        return JSONResponse({"error": "Bad rating."}, status_code=400)
    if difficulty is not None and difficulty not in ("",) + db.DIFFICULTIES:
        return JSONResponse({"error": "Bad difficulty."}, status_code=400)
    if rating is None and difficulty is None:
        return JSONResponse({"error": "Nothing to save."}, status_code=400)
    return {"ok": True, **db.set_feedback(user["id"], user["level"], day, q, rating, difficulty)}


HINT_UNLOCK = (2, 4, 6)  # run attempts needed before hint 1, 2, 3 can be opened


@app.post("/api/hint")
async def api_hint(request: Request):
    """Reveal the next hint (opt-in per user, and only after a few real attempts)."""
    user = current_user(request)
    if not user:
        return JSONResponse({"error": "Please log in."}, status_code=401)
    if not user["hints_on"]:
        return JSONResponse({"error": "Turn on hints in your account settings first."}, status_code=403)
    body = await request.json()
    try:
        day, q = int(body["day"]), int(body["q"])
    except (KeyError, ValueError, TypeError):
        return JSONResponse({"error": "Bad request."}, status_code=400)
    _, question = _get_question(user["level"], day, q)
    if not question or not question["hints"]:
        return JSONResponse({"error": "There are no hints for this question yet."}, status_code=404)
    row = db.get_question_progress(user["id"], user["level"], day, q)
    shown = row["hint_level"] if row else 0
    attempts = row["attempts"] if row else 0
    nxt = shown + 1
    if nxt > len(question["hints"]):
        return JSONResponse({"error": "You have seen every hint for this question."}, status_code=400)
    need = HINT_UNLOCK[nxt - 1] - attempts
    if need > 0:
        return JSONResponse({"error": f"Give it a bit more effort first: run your code {need} more time{'' if need == 1 else 's'} to unlock the next hint."},
                            status_code=403)
    db.raise_hint_level(user["id"], user["level"], day, q, nxt)
    db.log_event(user["id"], "hint_opened", f"{user['level']}:{day}:{q}:{nxt}")
    return {"hints": question["hints"][:nxt], "left": len(question["hints"]) - nxt}


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
