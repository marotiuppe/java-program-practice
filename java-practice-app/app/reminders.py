"""Opt-in email reminders and the weekly recap.

Rules (kept deliberately gentle):
  * only users who opted in AND have a confirmed email address;
  * at most ONE email per user per day, and only after REMINDER_HOUR (default 18:00) in the user's timezone;
  * "streak" nudge: practiced yesterday but not today yet;
  * "lapsed" nudge: exactly 3 days, and once more at 14 days, after the last practice day, then silence;
  * weekly recap on Sunday for anyone who practiced in the last 7 days;
  * every email carries a one-click unsubscribe link and the List-Unsubscribe headers.
Duplicate sends are prevented with rows in the events table, so a restart never re-sends."""
import logging
import os
from datetime import date, datetime, timedelta, timezone

from itsdangerous import BadSignature, URLSafeSerializer

from . import db, mailer, stats

log = logging.getLogger("reminders")

REMINDER_HOUR = int(os.environ.get("REMINDER_HOUR", "18"))
MAX_PER_RUN = int(os.environ.get("REMINDER_MAX_PER_RUN", "100"))  # stay well inside Gmail's daily limit
CHECK_SECONDS = int(os.environ.get("REMINDER_CHECK_SECONDS", "1800"))


def _signer():
    return URLSafeSerializer(os.environ.get("SECRET_KEY", "dev-only-change-me"), salt="unsubscribe")


def unsubscribe_token(user_id: int) -> str:
    return _signer().dumps(user_id)


def read_unsubscribe_token(token: str):
    try:
        return int(_signer().loads(token))
    except (BadSignature, ValueError, TypeError):
        return None


def choose(active: set, today: date, hour: int, solved_this_week: int):
    """Decide which email (if any) a user should get right now.
    Returns (kind, dedupe_key) or None. `active` is a set of date objects the user practiced."""
    if hour < REMINDER_HOUR or not active:
        return None
    last = max(active)
    gap = (today - last).days
    streak = stats.streak_v2([d.isoformat() for d in active], today)
    # Sunday recap for recently active learners (the ISO week makes it once a week)
    if today.weekday() == 6 and gap <= 6:
        iso = today.isocalendar()
        return "weekly", f"{iso[0]}-W{iso[1]:02d}"
    if today in active:
        return None
    if gap == 1 and streak["current"] >= 1:
        return "streak", today.isoformat()
    if gap in (3, 14):
        return "lapsed", f"{gap}:{last.isoformat()}"
    return None


def compose(kind: str, user: dict, ctx: dict, base_url: str, unsub_url: str):
    name_line = "Hi,\n\n"
    if kind == "streak":
        subject = f"Keep your {ctx['streak']}-day streak going"
        body = (f"{name_line}You practiced yesterday, so a single question today keeps your {ctx['streak']}-day streak alive.\n"
                f"It only takes a few minutes: {base_url}/dashboard\n")
    elif kind == "lapsed":
        subject = "Your Java practice is saved. Pick up where you left off"
        body = (f"{name_line}It has been a few days. Your progress and your place in the plan are exactly as you left them.\n"
                f"One question is enough to get going again: {base_url}/dashboard\n")
    else:
        subject = "Your week in Java practice"
        body = (f"{name_line}This week you solved {ctx['solved_week']} question{'' if ctx['solved_week'] == 1 else 's'} "
                f"and your current streak is {ctx['streak']} day{'' if ctx['streak'] == 1 else 's'}.\n"
                f"Next up: {base_url}/dashboard\n")
    footer = ("\n--\nYou get this because you turned on reminders for Java Practice.\n"
              f"Stop these emails with one click: {unsub_url}\n")
    return subject, body + footer


def run_once(base_url: str, now_utc: datetime | None = None) -> int:
    """Send whatever is due. Returns the number of emails sent."""
    if not mailer.configured():
        return 0
    now_utc = now_utc or datetime.utcnow()
    sent = 0
    for user in db.list_reminder_users():
        if sent >= MAX_PER_RUN:
            break
        try:
            zone = stats._load_zone(user["tz"]) or stats._load_zone(os.environ.get("APP_TIMEZONE", "Asia/Kolkata"))
            local = now_utc.replace(tzinfo=timezone.utc).astimezone(zone)
            active = {date.fromisoformat(d) for d in db.get_active_dates(user["id"])}
            week_start = int((local - timedelta(days=6)).replace(hour=0, minute=0, second=0, microsecond=0).timestamp())
            solved_week = db.count_solved_since(user["id"], week_start)
            pick = choose(active, local.date(), local.hour, solved_week)
            if not pick:
                continue
            kind, key = pick
            # one email a day, and never the same email twice
            if not db.log_event(user["id"], "reminder_day", local.date().isoformat(), once=True):
                continue
            if not db.log_event(user["id"], "reminder_sent", f"{kind}:{key}", once=True):
                continue
            streak = stats.streak_v2([d.isoformat() for d in active], local.date())["current"]
            unsub = f"{base_url}/unsubscribe/{unsubscribe_token(user['id'])}"
            subject, body = compose(kind, user, {"streak": streak, "solved_week": solved_week}, base_url, unsub)
            if mailer.send(user["email"], subject, body, headers={
                    "List-Unsubscribe": f"<{unsub}>", "List-Unsubscribe-Post": "List-Unsubscribe=One-Click"}):
                sent += 1
        except Exception:
            log.exception("reminder failed for user %s", user["id"])
    if sent:
        log.info("sent %d reminder email(s)", sent)
    return sent
