import asyncio
import logging
import os
import re
import time
import urllib.request
from contextlib import asynccontextmanager

from fastapi import FastAPI, Form, Request
from fastapi.responses import JSONResponse, RedirectResponse
from fastapi.staticfiles import StaticFiles
from fastapi.templating import Jinja2Templates
from starlette.middleware.sessions import SessionMiddleware

from . import content, db
from .runner import run_java

logging.basicConfig(level=logging.INFO)
log = logging.getLogger("app")
HERE = os.path.dirname(__file__)
templates = Jinja2Templates(directory=os.path.join(HERE, "templates"))

EMAIL_RE = re.compile(r"^[^@\s]+@[^@\s]+\.[^@\s]+$")
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


@asynccontextmanager
async def lifespan(app: FastAPI):
    db.init()
    content.load()
    task = None
    base = os.environ.get("KEEPALIVE_URL") or os.environ.get("RENDER_EXTERNAL_URL")
    if base:
        task = asyncio.create_task(_keepalive_loop(base.rstrip("/") + "/health"))
    yield
    if task:
        task.cancel()


app = FastAPI(lifespan=lifespan)
app.add_middleware(
    SessionMiddleware,
    secret_key=os.environ.get("SECRET_KEY", "dev-only-change-me"),
    same_site="lax",
    https_only=bool(os.environ.get("RENDER")),
    max_age=60 * 60 * 24 * 30,
)
app.mount("/static", StaticFiles(directory=os.path.join(HERE, "static")), name="static")


def current_user(request: Request):
    uid = request.session.get("uid")
    if not uid:
        return None
    user = db.get_user(uid)
    if user is None:
        request.session.clear()
    return user


def render(request: Request, name: str, **ctx):
    return templates.TemplateResponse(request, name, {"user": current_user(request), **ctx})


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
async def signup(request: Request, email: str = Form(...), password: str = Form(...)):
    email = email.strip().lower()
    if not EMAIL_RE.match(email):
        return render(request, "auth.html", mode="signup", error="Enter a valid email address.")
    if len(password) < 8:
        return render(request, "auth.html", mode="signup", error="Password must be at least 8 characters.")
    uid = db.create_user(email, password)
    if uid is None:
        return render(request, "auth.html", mode="signup", error="That email is already registered.")
    request.session["uid"] = uid
    return RedirectResponse("/level", status_code=303)


@app.get("/login")
async def login_page(request: Request):
    return render(request, "auth.html", mode="login", error=None)


@app.post("/login")
async def login(request: Request, email: str = Form(...), password: str = Form(...)):
    user = db.get_user_by_email(email.strip().lower())
    if not user or not db.verify_password(password, user["pw_hash"]):
        return render(request, "auth.html", mode="login", error="Wrong email or password.")
    request.session["uid"] = user["id"]
    return RedirectResponse("/", status_code=303)


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
        db.set_level(user["id"], level)
    return RedirectResponse("/dashboard", status_code=303)


@app.get("/dashboard")
async def dashboard(request: Request):
    user = current_user(request)
    if not user:
        return RedirectResponse("/login", status_code=303)
    level = user["level"]
    if level not in content.CONTENT:
        return RedirectResponse("/level", status_code=303)
    solved, attempted = {}, {}
    for row in db.get_progress(user["id"], level):
        if row["status"] == "solved":
            solved[row["day"]] = solved.get(row["day"], 0) + 1
        elif row["status"] == "attempted":
            attempted[row["day"]] = attempted.get(row["day"], 0) + 1
    days = []
    for n, day in sorted(content.CONTENT[level]["days"].items()):
        s, a = solved.get(n, 0), attempted.get(n, 0)
        state = "done" if s == 5 else "partial" if (s or a) else "todo"
        days.append({"n": n, "title": day["title"], "solved": s, "attempted": a, "state": state})
    total = sum(d["solved"] for d in days)
    return render(
        request, "dashboard.html", days=days, level_label=content.CONTENT[level]["label"], total_solved=total
    )


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
    return render(request, "day.html", day=d, status=status, last_day=max(content.CONTENT[user["level"]]["days"]))


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
    )


@app.post("/api/run")
async def api_run(request: Request):
    user = current_user(request)
    if not user:
        return JSONResponse({"error": "Please log in."}, status_code=401)
    now = time.monotonic()
    if now - _last_run.get(user["id"], 0) < 1.5:
        return JSONResponse({"stage": "busy", "ok": False, "error": "Slow down a little."}, status_code=429)
    _last_run[user["id"]] = now
    body = await request.json()
    code = str(body.get("code", ""))
    stdin_text = str(body.get("stdin", ""))
    return JSONResponse(await run_java(code, stdin_text))


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
    return {"ok": True}
