"""Practice-streak helpers. "Today" uses the learner's browser timezone when the request carries one
(see set_request_timezone), otherwise APP_TIMEZONE (default Asia/Kolkata)."""
import os
from contextvars import ContextVar
from datetime import date, datetime, timedelta, timezone

try:
    from zoneinfo import ZoneInfo
except ImportError:  # pragma: no cover
    ZoneInfo = None


_request_tz = ContextVar("request_tz", default=None)


def _load_zone(name):
    if ZoneInfo and name:
        try:
            return ZoneInfo(name)
        except Exception:
            pass
    return None


def set_request_timezone(name) -> None:
    """Use this IANA zone (e.g. "Europe/London") for the rest of the current request; ignored if invalid."""
    _request_tz.set(_load_zone(name))


def _tz():
    return _request_tz.get() or _load_zone(os.environ.get("APP_TIMEZONE", "Asia/Kolkata")) or timezone.utc


def zone():
    """The timezone in effect for this request."""
    return _tz()


def today() -> date:
    return datetime.now(_tz()).date()


def today_str() -> str:
    return today().isoformat()


def today_start_epoch() -> int:
    """Unix time of local midnight (APP_TIMEZONE) at the start of today."""
    return int(datetime.combine(today(), datetime.min.time(), tzinfo=_tz()).timestamp())


def compute(date_strings, today_date=None) -> dict:
    """current: consecutive days ending today (or yesterday, so the streak is not lost until the day ends);
    longest: best run ever; total: number of active days."""
    days = set()
    for s in date_strings:
        try:
            days.add(date.fromisoformat(s))
        except ValueError:
            continue
    today_date = today_date or today()
    anchor = today_date if today_date in days else today_date - timedelta(days=1)
    current = 0
    d = anchor
    while d in days:
        current += 1
        d -= timedelta(days=1)
    longest = run = 0
    prev = None
    for d in sorted(days):
        run = run + 1 if prev is not None and d - prev == timedelta(days=1) else 1
        longest = max(longest, run)
        prev = d
    return {"current": current, "longest": longest, "total": len(days), "today": today_date in days}


def streak_v2(date_strings, today_date=None) -> dict:
    """Streak with forgiveness, computed purely from the dates the learner was active.

    - Every 7th real day of a streak earns a freeze (at most 2 banked).
    - A missed day uses a banked freeze automatically: the streak survives but does not grow.
    - With no freeze left, a missed day resets the streak. Today never counts as missed until it is over.
    Returns current, longest, total, today (active today?), freezes (banked), frozen (dates a freeze covered)
    and next_freeze_in (real days until the next freeze is earned, None when the bank is full)."""
    active = set()
    for s in date_strings:
        try:
            active.add(date.fromisoformat(s))
        except ValueError:
            continue
    today_date = today_date or today()
    out = {"current": 0, "longest": 0, "total": len(active), "today": today_date in active,
           "freezes": 0, "frozen": [], "next_freeze_in": 7}
    if not active:
        return out
    streak = banked = longest = 0
    frozen = []
    d = min(active)
    while d <= today_date:
        if d in active:
            streak += 1
            if streak % 7 == 0:
                banked = min(2, banked + 1)
        elif d < today_date:
            if streak > 0 and banked > 0:
                banked -= 1
                frozen.append(d)
            else:
                streak = 0
        longest = max(longest, streak)
        d += timedelta(days=1)
    out.update(current=streak, longest=longest, freezes=banked, frozen=frozen,
               next_freeze_in=None if banked >= 2 else 7 - streak % 7)
    return out
