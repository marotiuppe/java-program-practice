"""Practice-streak helpers. Dates use APP_TIMEZONE (default Asia/Kolkata), so "today" matches the learner."""
import os
from datetime import date, datetime, timedelta, timezone

try:
    from zoneinfo import ZoneInfo
except ImportError:  # pragma: no cover
    ZoneInfo = None


def _tz():
    name = os.environ.get("APP_TIMEZONE", "Asia/Kolkata")
    if ZoneInfo:
        try:
            return ZoneInfo(name)
        except Exception:
            pass
    return timezone.utc


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
