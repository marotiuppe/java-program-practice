"""XP, learner level and badges. Everything is computed from progress, events and streaks on demand,
so nothing extra is stored and the rules can change without a migration."""
from math import isqrt

XP_PER_SOLVE = 10
XP_PER_DAY = 50
XP_PER_LEVEL = 300

# (key, title, how to earn it)
BADGES = [
    ("first_run", "First Run", "Run a program for the first time"),
    ("first_solve", "First Solve", "Solve your first question"),
    ("streak7", "7-Day Streak", "Practice 7 days in a row"),
    ("day_complete", "Day Complete", "Solve all 5 questions of a day"),
    ("week_complete", "Week Complete", "Complete every day of a plan week (Days 1-7, 8-14, 15-21 or 22-28)"),
    ("level_complete", "Level Complete", "Solve all 150 questions of a level"),
    ("debug_detective", "Debug Detective", "Solve 10 Debug questions"),
    ("predictor", "Predictor", "Solve 10 Predict-the-output questions"),
]


def learner_level(xp: int) -> dict:
    """Level L is reached at 50*(L-1)^2 XP: 50, 200, 450, 800, ..."""
    lvl = 1 + isqrt(xp // 50)
    floor, ceil = 50 * (lvl - 1) ** 2, 50 * lvl ** 2
    return {"level": lvl, "xp": xp, "floor": floor, "next": ceil,
            "pct": round(100 * (xp - floor) / (ceil - floor))}


def compute(rows, kind_of, days_per_level: dict, longest_streak: int, has_first_run: bool) -> dict:
    """rows: iterable of (level, day, qnum, status) for one user.
    kind_of(level, day, qnum) -> 'code' | 'predict' | 'debug'.
    days_per_level: {level: number_of_days}. Returns {"xp", "learner", "badges": [...]}."""
    solved = {}  # (level, day) -> set of solved question numbers
    kinds = {"predict": 0, "debug": 0}
    for level, day, q, status in rows:
        if status != "solved":
            continue
        solved.setdefault((level, day), set()).add(q)
        k = kind_of(level, day, q)
        if k in kinds:
            kinds[k] += 1
    total = sum(len(v) for v in solved.values())
    done_days = {key for key, v in solved.items() if len(v) >= 5}
    level_done = [lv for lv, n in days_per_level.items() if all((lv, d) in done_days for d in range(1, n + 1))]
    week_done = any(
        all((lv, d) in done_days for d in range(start, start + 7))
        for lv, n in days_per_level.items() for start in range(1, n - 5, 7)
    )
    xp = total * XP_PER_SOLVE + len(done_days) * XP_PER_DAY + len(level_done) * XP_PER_LEVEL
    earned = {
        "first_run": has_first_run,
        "first_solve": total >= 1,
        "streak7": longest_streak >= 7,
        "day_complete": bool(done_days),
        "week_complete": week_done,
        "level_complete": bool(level_done),
        "debug_detective": kinds["debug"] >= 10,
        "predictor": kinds["predict"] >= 10,
    }
    return {
        "xp": xp,
        "learner": learner_level(xp),
        "solved_total": total,
        "badges": [{"key": k, "title": t, "how": h, "earned": earned[k]} for k, t, h in BADGES],
    }
