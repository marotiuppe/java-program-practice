"""In-memory sliding-window rate limiter.

State lives in this process only, which is correct for the single-instance deployment. If the app is ever
run on several instances, each instance enforces its own limits (so the effective limit is higher)."""
import time
from collections import deque

_hits = {}  # key -> deque of timestamps
_PRUNE_EVERY = 500
_calls = 0


def _clock() -> float:
    return time.monotonic()


def _window(key: str, window: float, now: float) -> deque:
    q = _hits.setdefault(key, deque())
    while q and now - q[0] >= window:
        q.popleft()
    return q


def _maybe_prune(now: float, longest: float = 3600.0) -> None:
    global _calls
    _calls += 1
    if _calls % _PRUNE_EVERY:
        return
    for k in [k for k, q in _hits.items() if not q or now - q[-1] >= longest]:
        del _hits[k]


def record(key: str) -> None:
    """Count one event for this key (use for failures you want to limit, like wrong passwords)."""
    now = _clock()
    _hits.setdefault(key, deque()).append(now)
    _maybe_prune(now)


def retry_after(key: str, limit: int, window: float) -> int:
    """Seconds until another event is allowed, or 0 if the key is under its limit right now."""
    now = _clock()
    q = _window(key, window, now)
    if len(q) < limit:
        return 0
    return max(1, int(window - (now - q[len(q) - limit])) + 1)


def hit(key: str, limit: int, window: float) -> int:
    """Check and count in one step. Returns 0 if allowed (and counts it), else seconds to wait."""
    wait = retry_after(key, limit, window)
    if wait == 0:
        record(key)
    return wait


def reset() -> None:
    """Forget everything (used by tests)."""
    _hits.clear()


def human(seconds: int) -> str:
    if seconds < 90:
        return f"{seconds} seconds"
    return f"{round(seconds / 60)} minutes"
