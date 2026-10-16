"""Branded emails. Every message has an HTML version (logo, banner, one clear button, footer) and a plain-text
version built from the same content, so the two always say the same thing.

    subject, text, html = render("streak", base_url, streak=4, goal=(1, 3), next={...}, unsub=url)

Kinds: verify, reset, changed, streak, lapsed, weekly, test. To change the artwork see tools/make_email_images.py;
to preview every email run tools/preview_emails.py."""
import os

from jinja2 import Environment, FileSystemLoader, select_autoescape

_env = Environment(
    loader=FileSystemLoader(os.path.join(os.path.dirname(__file__), "templates", "email")),
    autoescape=select_autoescape(["html"]),
)

REASONS = {
    "verify": "You are receiving this because someone signed up for Java Practice with this email address.",
    "reset": "You are receiving this because a password reset was requested for your Java Practice account.",
    "changed": "This is a security notice about your Java Practice account.",
    "reminder": "You are receiving this because you turned on reminder emails. You can turn them off at any time.",
    "test": "This is a test message sent from the Java Practice admin page.",
}


def _next(n):
    return {"day": n["day"], "q": n["q"], "title": n.get("title", ""), "category": n.get("category", "")} if n else None


def build(kind: str, base: str, **c) -> dict:
    """The content of one email as plain data (also used to make the text version)."""
    base = base.rstrip("/")
    nxt = _next(c.get("next"))
    go_next = f"{base}/day/{nxt['day']}/q/{nxt['q']}" if nxt else f"{base}/dashboard"
    e = {"kind": kind, "stats": [], "next": None, "goal": None, "note": None, "unsub": None, "button": None}

    if kind == "verify":
        e.update(
            subject="Confirm your email for Java Practice", hero="hero-verify.png", hero_alt="Email confirmation",
            preheader="One tap and you are in. This link works for 24 hours.",
            heading="Confirm your email",
            paras=["Welcome to Java Practice! You are one tap away from writing your first program.",
                   "Confirm your email address so we can help you get back into your account if you ever forget "
                   "your password."],
            button={"label": "Confirm my email", "url": c["link"]},
            note="This link works for 24 hours. If you did not create an account, you can safely ignore this email.",
            reason=REASONS["verify"])
    elif kind == "reset":
        e.update(
            subject="Reset your Java Practice password", hero="hero-reset.png", hero_alt="A key",
            preheader="Choose a new password. This link works for 30 minutes.",
            heading="Reset your password",
            paras=["We got a request to reset the password for your account. Use the button below to choose a new "
                   "one. The link works for 30 minutes and can be used once."],
            button={"label": "Choose a new password", "url": c["link"]},
            note="Did not ask for this? Ignore this email. Your password stays the same and nobody can use "
                 "this link without access to your inbox.",
            reason=REASONS["reset"])
    elif kind == "changed":
        e.update(
            subject="Your Java Practice password was changed", hero="hero-security.png", hero_alt="A shield",
            preheader="If this was not you, reset your password right away.",
            heading="Your password was changed",
            paras=["The password for your Java Practice account was just changed.",
                   "If this was you, there is nothing more to do. If it was not, reset your password straight away "
                   "so you stay in control of your account."],
            button={"label": "Reset my password", "url": f"{base}/forgot"},
            reason=REASONS["changed"])
    elif kind == "streak":
        n, done, goal = c["streak"], c.get("solved_today", 0), c.get("goal", 1)
        e.update(
            subject=f"Keep your {n}-day streak going \U0001F525", hero="hero-streak.gif", hero_alt="A flame",
            preheader=f"One question today keeps your {n}-day streak alive. It takes a few minutes.",
            heading=f"Keep your {n}-day streak going",
            paras=[f"You practiced yesterday, so one question today keeps your {n}-day streak alive. "
                   "It only takes a few minutes."],
            goal={"done": done, "total": goal, "pct": min(100, round(100 * done / goal)) if goal else 0},
            next=nxt, button={"label": "Practice now", "url": go_next}, unsub=c.get("unsub"),
            reason=REASONS["reminder"])
    elif kind == "lapsed":
        e.update(
            subject="Your Java practice is saved. Pick up where you left off", hero="hero-lapsed.png", hero_alt="A wave",
            preheader="Your progress is exactly as you left it. One question is enough to start again.",
            heading="We saved your place",
            paras=["It has been a few days, and that is completely fine. Your progress and your spot in the plan are "
                   "exactly as you left them.",
                   "One question is enough to get going again."],
            stats=[{"value": c.get("solved_total", 0), "label": "questions solved so far"}] if c.get("solved_total") else [],
            next=nxt, button={"label": "Pick up where I left off", "url": go_next}, unsub=c.get("unsub"),
            reason=REASONS["reminder"])
    elif kind == "weekly":
        w, n = c["solved_week"], c["streak"]
        e.update(
            subject="Your week in Java practice", hero="hero-weekly.png", hero_alt="A rising chart",
            preheader=f"You solved {w} question{'' if w == 1 else 's'} this week. Here is where you are.",
            heading="Your week in Java practice",
            paras=["Here is how this week went. Small, steady practice is what makes Java stick."],
            stats=[{"value": w, "label": "solved this week"},
                   {"value": n, "label": f"day streak"},
                   {"value": c.get("solved_total", 0), "label": "solved in total"}],
            next=nxt, button={"label": "Continue practicing", "url": go_next}, unsub=c.get("unsub"),
            note=(f"You also have {c['review_due']} question{'' if c['review_due'] == 1 else 's'} ready to review."
                  if c.get("review_due") else None),
            reason=REASONS["reminder"])
    elif kind == "test":
        e.update(
            subject="Java Practice test email", hero="hero-test.png", hero_alt="A check mark",
            preheader="If you can read this, email sending works.",
            heading="Email is working",
            paras=["This is a test email from the Java Practice admin page. If you can read this, your email "
                   "settings are correct."],
            button={"label": "Open the admin page", "url": f"{base}/admin/system"}, reason=REASONS["test"])
    else:
        raise ValueError(f"unknown email kind: {kind}")
    return e


def to_text(e: dict, base: str) -> str:
    out = [e["heading"], "=" * len(e["heading"]), ""]
    for p in e["paras"]:
        out += [p, ""]
    if e["goal"]:
        out += [f"Today's goal: {e['goal']['done']} of {e['goal']['total']} solved", ""]
    for s in e["stats"]:
        out.append(f"{s['value']} {s['label']}")
    if e["stats"]:
        out.append("")
    if e["next"]:
        n = e["next"]
        out += [f"Your next question: Day {n['day']}, Question {n['q']} - {n['title']}", ""]
    if e["button"]:
        out += [f"{e['button']['label']}:", e["button"]["url"], ""]
    if e["note"]:
        out += [e["note"], ""]
    out += ["--", "Java Practice - a free 30-day Java practice plan", f"Open the app: {base.rstrip('/')}/dashboard",
            f"Privacy: {base.rstrip('/')}/privacy"]
    if e["unsub"]:
        out.append(f"Unsubscribe: {e['unsub']}")
    out += ["", e["reason"], ""]
    return "\n".join(out)


def render(kind: str, base: str, **c):
    """(subject, plain text, html) for one email."""
    e = build(kind, base, **c)
    html = _env.get_template("layout.html").render(e=e, base=base.rstrip("/"))
    return e["subject"], to_text(e, base), html
