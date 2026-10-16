"""Admin area (/admin/...): overview, users, one user's progress, issue reports, question quality, activity feed
and system status. Only accounts listed in ADMIN_EMAILS with a confirmed email can open these pages.

SQL here is written once for both SQLite and Postgres (no date functions or literal % signs); dates are bucketed
in Python."""
import os
import platform
import time
from datetime import date, datetime, timedelta

from fastapi import APIRouter, BackgroundTasks, Form, Request
from fastapi.responses import JSONResponse, RedirectResponse

from . import badges, content, db, mailer, runner, sandbox, stats

router = APIRouter(prefix="/admin")
STARTED = time.time()
PAGE = 25

MESSAGES = {
    "verified": "Email marked as confirmed.",
    "reset": "Password reset email sent.",
    "noreset": "Could not send: email sending is not configured on this server.",
    "deleted": "Account deleted.",
    "badconfirm": "Deletion cancelled: the email you typed did not match.",
    "protected": "Admin accounts (and your own) cannot be deleted from here.",
    "testok": "Test email sent. Check the inbox (and spam folder) of your admin address.",
    "testfail": "The test email could not be sent. See 'Last email error' below.",
    "resolved": "Report marked resolved.",
    "reopened": "Report reopened.",
}


# ---------------------------------------------------------------- helpers
def _guard(request: Request):
    """(user, None) for an admin; (None, response) otherwise."""
    from . import main as m

    user = m.current_user(request)
    if not user:
        return None, RedirectResponse("/login", status_code=303)
    if not m.is_admin(user):
        return None, JSONResponse({"error": "Not found."}, status_code=404)
    return user, None


def _render(request: Request, template: str, **ctx):
    from . import main as m

    return m.render(request, template, admin_area=True, flash=MESSAGES.get(request.query_params.get("msg", "")), **ctx)


def _q(sql: str, params=()):
    with db.connect() as c:
        return db._x(c, sql, params).fetchall()


def _one(sql: str, params=()) -> int:
    rows = _q(sql, params)
    return int(rows[0]["n"] or 0) if rows else 0


def _series(day_counts: dict, days: int):
    """Last `days` days as bar-chart data: [{label, n, pct}] oldest first."""
    today = stats.today()
    items = []
    for i in range(days - 1, -1, -1):
        d = today - timedelta(days=i)
        items.append({"label": d.strftime("%d %b"), "n": day_counts.get(d.isoformat(), 0)})
    top = max([x["n"] for x in items] + [1])
    for x in items:
        x["pct"] = round(100 * x["n"] / top)
    return items


def _epochs_by_day(epochs):
    zone = stats.zone()
    out = {}
    for t in epochs:
        d = datetime.fromtimestamp(t, zone).date().isoformat()
        out[d] = out.get(d, 0) + 1
    return out


def _pct(part: int, whole: int) -> int:
    return round(100 * part / whole) if whole else 0


# ---------------------------------------------------------------- overview
def _retention(today: date):
    """Share of recent sign-ups who came back exactly 1 and 7 days after the day they joined."""
    zone = stats.zone()
    since = int(time.time()) - 60 * 86400
    users = _q("SELECT id, created_at FROM users WHERE created_at >= ?", (since,))
    joined = {u["id"]: datetime.fromtimestamp(u["created_at"], zone).date() for u in users}
    active = {}
    for r in _q("SELECT a.user_id, a.day FROM activity a JOIN users u ON u.id = a.user_id WHERE u.created_at >= ?",
                (since,)):
        active.setdefault(r["user_id"], set()).add(r["day"])
    out = {}
    for n in (1, 7):
        eligible = [uid for uid, d in joined.items() if d + timedelta(days=n) <= today]
        kept = [uid for uid in eligible if (joined[uid] + timedelta(days=n)).isoformat() in active.get(uid, ())]
        out[n] = {"eligible": len(eligible), "kept": len(kept), "pct": _pct(len(kept), len(eligible))}
    return out


@router.get("")
async def overview(request: Request):
    user, bad = _guard(request)
    if bad:
        return bad
    today = stats.today()
    day = lambda n: (today - timedelta(days=n)).isoformat()
    now = int(time.time())
    total = _one("SELECT COUNT(*) AS n FROM users")
    funnel = [
        ("Signed up", total),
        ("Chose a level", _one("SELECT COUNT(*) AS n FROM users WHERE level != ''")),
        ("Ran code", _one("SELECT COUNT(DISTINCT user_id) AS n FROM events WHERE name = 'first_run'")),
        ("Solved a question", _one("SELECT COUNT(DISTINCT user_id) AS n FROM progress WHERE status = 'solved'")),
        ("Completed a day", _one("SELECT COUNT(DISTINCT user_id) AS n FROM events WHERE name = 'day_complete'")),
        ("Practiced 7+ days", _one("SELECT COUNT(*) AS n FROM (SELECT user_id FROM activity GROUP BY user_id "
                                   "HAVING COUNT(*) >= 7) t")),
    ]
    funnel = [{"label": l, "n": n, "pct": _pct(n, total)} for l, n in funnel]
    kpis = {
        "users": total,
        "new_7": _one("SELECT COUNT(*) AS n FROM users WHERE created_at >= ?", (now - 7 * 86400,)),
        "new_30": _one("SELECT COUNT(*) AS n FROM users WHERE created_at >= ?", (now - 30 * 86400,)),
        "verified": _one("SELECT COUNT(*) AS n FROM users WHERE email_verified = 1"),
        "dau": _one("SELECT COUNT(DISTINCT user_id) AS n FROM activity WHERE day = ?", (day(0),)),
        "wau": _one("SELECT COUNT(DISTINCT user_id) AS n FROM activity WHERE day >= ?", (day(6),)),
        "mau": _one("SELECT COUNT(DISTINCT user_id) AS n FROM activity WHERE day >= ?", (day(29),)),
        "solves": _one("SELECT COUNT(*) AS n FROM progress WHERE status = 'solved'"),
        "open_reports": _one("SELECT COUNT(*) AS n FROM reports WHERE resolved = 0"),
    }
    dau = {r["day"]: int(r["n"]) for r in _q(
        "SELECT day, COUNT(*) AS n FROM activity WHERE day >= ? GROUP BY day", (day(13),))}
    signups = _epochs_by_day(r["created_at"] for r in _q(
        "SELECT created_at FROM users WHERE created_at >= ?", (now - 15 * 86400,)))
    solves = _epochs_by_day(r["solved_at"] for r in _q(
        "SELECT solved_at FROM progress WHERE solved_at IS NOT NULL AND solved_at >= ?", (now - 15 * 86400,)))
    levels = [{"label": info["label"], "n": _one("SELECT COUNT(*) AS n FROM users WHERE level = ?", (key,))}
              for key, info in content.CONTENT.items()]
    return _render(request, "admin_overview.html", page="overview", kpis=kpis, funnel=funnel,
                   retention=_retention(today), levels=levels,
                   charts=[("Active users per day", _series(dau, 14)), ("New sign-ups per day", _series(signups, 14)),
                           ("Questions solved per day", _series(solves, 14))])


# ---------------------------------------------------------------- users
SORTS = {
    "newest": "u.id DESC",
    "oldest": "u.id ASC",
    "active": "last_active DESC, u.id DESC",
    "solved": "solved DESC, u.id DESC",
    "email": "u.email ASC",
}


@router.get("/users")
async def users(request: Request, q: str = "", sort: str = "newest", page: int = 1):
    user, bad = _guard(request)
    if bad:
        return bad
    sort = sort if sort in SORTS else "newest"
    page = max(1, page)
    needle = "%" + q.strip().lower().replace("%", "").replace("_", "") + "%"
    total = _one("SELECT COUNT(*) AS n FROM users u WHERE u.email LIKE ?", (needle,))
    rows = _q(
        "SELECT u.id, u.email, u.email_verified, u.level, u.daily_goal, u.created_at, "
        "(SELECT COUNT(*) FROM progress p WHERE p.user_id = u.id AND p.status = 'solved') AS solved, "
        "(SELECT MAX(a.day) FROM activity a WHERE a.user_id = u.id) AS last_active, "
        "(SELECT COUNT(*) FROM reports r WHERE r.user_id = u.id) AS reports "
        f"FROM users u WHERE u.email LIKE ? ORDER BY {SORTS[sort]} LIMIT ? OFFSET ?",
        (needle, PAGE, (page - 1) * PAGE))
    pages = max(1, -(-total // PAGE))
    return _render(request, "admin_users.html", page="users", users=rows, q=q, sort=sort, sorts=list(SORTS),
                   total=total, pg=page, pages=pages)


@router.get("/users/{uid}")
async def user_detail(request: Request, uid: int):
    me, bad = _guard(request)
    if bad:
        return bad
    from . import main as m

    target = db.get_user(uid)
    if not target:
        return JSONResponse({"error": "No such user."}, status_code=404)
    data = db.export_user(uid)
    active = db.get_active_dates(uid)
    streak = stats.streak_v2(active)
    days_per_level = {lv: len(info["days"]) for lv, info in content.CONTENT.items()}
    result = badges.compute(db.get_all_progress(uid), m._kind_of, days_per_level, streak["longest"],
                            db.has_event(uid, "first_run", ""))
    levels = []
    for key, info in content.CONTENT.items():
        rows = [r for r in data["progress"] if r["level"] == key]
        if not rows and target["level"] != key:
            continue
        per_day = {}
        for r in rows:
            cell = per_day.setdefault(r["day"], {"solved": 0, "attempted": 0})
            if r["status"] == "solved":
                cell["solved"] += 1
            elif r["status"] == "attempted":
                cell["attempted"] += 1
        grid = [{"n": n, **per_day.get(n, {"solved": 0, "attempted": 0})} for n in sorted(info["days"])]
        solved = sum(c["solved"] for c in grid)
        levels.append({
            "key": key, "label": info["label"], "solved": solved, "total": 5 * len(info["days"]),
            "days_done": sum(1 for c in grid if c["solved"] >= 5), "grid": grid,
            "attempts": sum(r["attempts"] for r in rows), "hints": sum(1 for r in rows if r["hint_level"]),
        })
    last_active = max(active) if active else None
    return _render(
        request, "admin_user.html", page="users", t=target, account=data["account"], streak=streak, levels=levels,
        xp=result, last_active=last_active, events=list(reversed(data["events"]))[:40], reports=data["reports"],
        reviews_due=len(data["reviews"]), is_admin_account=m.is_admin(target) or target["id"] == me["id"],
        mail_ready=mailer.configured())


@router.post("/users/{uid}/verify")
async def user_verify(request: Request, uid: int):
    user, bad = _guard(request)
    if bad:
        return bad
    if db.get_user(uid):
        db.mark_verified(uid)
    return RedirectResponse(f"/admin/users/{uid}?msg=verified", status_code=303)


@router.post("/users/{uid}/reset")
async def user_reset(request: Request, background: BackgroundTasks, uid: int):
    user, bad = _guard(request)
    if bad:
        return bad
    from . import main as m

    target = db.get_user(uid)
    if not target or not mailer.configured():
        return RedirectResponse(f"/admin/users/{uid}?msg=noreset", status_code=303)
    token = db.create_token(uid, "reset", m.RESET_TTL)
    background.add_task(m._send_reset, target["email"], f"{m.base_url(request)}/reset/{token}")
    return RedirectResponse(f"/admin/users/{uid}?msg=reset", status_code=303)


@router.post("/users/{uid}/delete")
async def user_delete(request: Request, uid: int, confirm_email: str = Form("")):
    me, bad = _guard(request)
    if bad:
        return bad
    from . import main as m

    target = db.get_user(uid)
    if not target:
        return RedirectResponse("/admin/users", status_code=303)
    if m.is_admin(target) or target["id"] == me["id"] or target["email"].lower() in m.ADMIN_EMAILS:
        return RedirectResponse(f"/admin/users/{uid}?msg=protected", status_code=303)
    if confirm_email.strip().lower() != target["email"].lower():
        return RedirectResponse(f"/admin/users/{uid}?msg=badconfirm", status_code=303)
    m.log.info("admin %s deleted user %s", me["id"], uid)
    db.delete_user(uid)
    return RedirectResponse("/admin/users?msg=deleted", status_code=303)


# ---------------------------------------------------------------- reports
@router.get("/reports")
async def reports(request: Request, status: str = "open", category: str = ""):
    user, bad = _guard(request)
    if bad:
        return bad
    status = status if status in ("open", "resolved", "all") else "open"
    where, params = [], []
    if status != "all":
        where.append("r.resolved = ?")
        params.append(0 if status == "open" else 1)
    if category:
        where.append("r.category = ?")
        params.append(category)
    rows = _q("SELECT r.*, u.email FROM reports r LEFT JOIN users u ON u.id = r.user_id "
              + ("WHERE " + " AND ".join(where) + " " if where else "") + "ORDER BY r.id DESC LIMIT 200", params)
    by_cat = {r["category"]: int(r["n"]) for r in _q(
        "SELECT category, COUNT(*) AS n FROM reports WHERE resolved = 0 GROUP BY category")}
    counts = {"open": _one("SELECT COUNT(*) AS n FROM reports WHERE resolved = 0"),
              "resolved": _one("SELECT COUNT(*) AS n FROM reports WHERE resolved = 1")}
    return _render(request, "admin_reports.html", page="reports", reports=rows, status=status, category=category,
                   by_cat=by_cat, counts=counts)


@router.post("/reports/{report_id}/resolve")
async def report_resolve(request: Request, report_id: int):
    user, bad = _guard(request)
    if bad:
        return bad
    db.resolve_report(report_id)
    return RedirectResponse("/admin/reports?msg=resolved", status_code=303)


@router.post("/reports/{report_id}/reopen")
async def report_reopen(request: Request, report_id: int):
    user, bad = _guard(request)
    if bad:
        return bad
    with db.connect() as c:
        db._x(c, "UPDATE reports SET resolved = 0 WHERE id = ?", (report_id,))
    return RedirectResponse("/admin/reports?status=resolved", status_code=303)


# ---------------------------------------------------------------- question quality
QSORTS = ("hardest", "attempts", "reports", "hints")


@router.get("/questions")
async def questions(request: Request, level: str = "beginner", sort: str = "hardest", min_learners: int = 2):
    user, bad = _guard(request)
    if bad:
        return bad
    level = level if level in content.CONTENT else "beginner"
    sort = sort if sort in QSORTS else "hardest"
    stat = {}
    for r in _q("SELECT day, qnum, COUNT(*) AS learners, SUM(attempts) AS attempts, "
                "SUM(CASE WHEN status = 'solved' THEN 1 ELSE 0 END) AS solved, "
                "SUM(CASE WHEN hint_level > 0 THEN 1 ELSE 0 END) AS hinted "
                "FROM progress WHERE level = ? GROUP BY day, qnum", (level,)):
        stat[(r["day"], r["qnum"])] = {k: int(r[k] or 0) for k in ("learners", "attempts", "solved", "hinted")}
    rep = {(r["day"], r["qnum"]): {"reports": int(r["n"]), "open": int(r["open_n"] or 0)} for r in _q(
        "SELECT day, qnum, COUNT(*) AS n, SUM(CASE WHEN resolved = 0 THEN 1 ELSE 0 END) AS open_n "
        "FROM reports WHERE level = ? GROUP BY day, qnum", (level,))}
    rows = []
    for key in sorted(set(stat) | set(rep)):
        d, qn = key
        q = content.CONTENT[level]["days"].get(d, {}).get("questions", {}).get(qn)
        s = stat.get(key, {"learners": 0, "attempts": 0, "solved": 0, "hinted": 0})
        rp = rep.get(key, {"reports": 0, "open": 0})
        rows.append({
            "day": d, "q": qn, "category": q["category"] if q else "?", "kind": q["kind"] if q else "",
            **s, **rp,
            "solve_rate": _pct(s["solved"], s["learners"]),
            "per_learner": round(s["attempts"] / s["learners"], 1) if s["learners"] else 0,
        })
    rows = [r for r in rows if r["learners"] >= min_learners or r["reports"]]
    order = {"hardest": lambda r: (r["solve_rate"], -r["per_learner"]), "attempts": lambda r: -r["per_learner"],
             "reports": lambda r: -r["reports"], "hints": lambda r: -r["hinted"]}[sort]
    rows.sort(key=order)
    content_stats = {
        "total": sum(len(d["questions"]) for d in content.CONTENT[level]["days"].values()),
        "checked": sum(1 for d in content.CONTENT[level]["days"].values() for q in d["questions"].values()
                       if q["check_count"]),
        "hinted": sum(1 for d in content.CONTENT[level]["days"].values() for q in d["questions"].values()
                      if q.get("hints")),
    }
    return _render(request, "admin_questions.html", page="questions", rows=rows[:60], level=level, sort=sort,
                   min_learners=min_learners, levels=[(k, v["label"]) for k, v in content.CONTENT.items()],
                   content_stats=content_stats, shown=len(rows))


# ---------------------------------------------------------------- activity feed
@router.get("/activity")
async def activity(request: Request, event: str = ""):
    user, bad = _guard(request)
    if bad:
        return bad
    since = int(time.time()) - 7 * 86400
    counts = [(r["name"], int(r["n"])) for r in _q(
        "SELECT name, COUNT(*) AS n FROM events WHERE created_at >= ? GROUP BY name ORDER BY n DESC", (since,))]
    feed = _q("SELECT e.id, e.user_id, e.name, e.meta, e.created_at, u.email FROM events e "
              "LEFT JOIN users u ON u.id = e.user_id "
              + ("WHERE e.name = ? " if event else "") + "ORDER BY e.id DESC LIMIT 100", (event,) if event else ())
    return _render(request, "admin_activity.html", page="activity", feed=feed, counts=counts, event=event)


# ---------------------------------------------------------------- system
@router.post("/system/test-email")
async def test_email(request: Request):
    """Send a test message to the admin's own address, so email settings can be checked in one click."""
    import asyncio

    user, bad = _guard(request)
    if bad:
        return bad
    ok = mailer.configured() and await asyncio.to_thread(
        mailer.send, user["email"], "Java Practice test email",
        "This is a test email from the Java Practice admin page.\nIf you can read this, email sending works.\n")
    return RedirectResponse(f"/admin/system?msg={'testok' if ok else 'testfail'}", status_code=303)


@router.get("/system")
async def system(request: Request):
    user, bad = _guard(request)
    if bad:
        return bad
    from . import main as m
    from . import reminders

    questions = [q for lv in content.CONTENT.values() for d in lv["days"].values() for q in d["questions"].values()]
    info = [
        ("Database", "Postgres" if db.PG else "SQLite (local file, wiped on redeploy on the free plan)"),
        ("Sandbox", sandbox.STATUS),
        ("Email sending", mailer.backend() if mailer.configured()
         else "NOT configured: verification and reset emails are disabled"),
        ("Public URL (APP_BASE_URL)", os.environ.get("APP_BASE_URL") or os.environ.get("RENDER_EXTERNAL_URL") or "not set"),
        ("Reminder emails", f"after {reminders.REMINDER_HOUR}:00 local time, checked every {reminders.CHECK_SECONDS // 60} min"
         if mailer.configured() else "off (needs email)"),
        ("Admin accounts", ", ".join(sorted(m.ADMIN_EMAILS)) or "none"),
        ("Programs waiting for the runner", str(runner.queue_depth())),
        ("Questions", f"{len(questions)} total, {sum(1 for q in questions if q['check_count'])} with checks, "
                      f"{sum(1 for q in questions if q.get('hints'))} with hints"),
        ("Server time (UTC)", datetime.utcnow().strftime("%Y-%m-%d %H:%M:%S")),
        ("Uptime", f"{int((time.time() - STARTED) // 3600)} h {int((time.time() - STARTED) % 3600 // 60)} min"),
        ("Version", (os.environ.get("RENDER_GIT_COMMIT") or "unknown")[:7]),
        ("Python", platform.python_version()),
    ]
    return _render(request, "admin_system.html", page="system", info=info, mail_ready=mailer.configured(),
                   last_mail_error=mailer.last_error)
