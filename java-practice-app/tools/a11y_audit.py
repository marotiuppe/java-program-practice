"""Automated accessibility audit (axe-core in headless Chrome) of the main pages, in light and dark mode.

    pip install playwright axe-playwright-python
    python tools/a11y_audit.py            # prints violations, exits 1 if there are any

Starts the app itself on a throwaway database. Automated checks catch roughly a third of accessibility problems
(contrast, missing labels, bad ARIA, headings, landmarks); keyboard and screen-reader behaviour still need a manual
pass. See the "Accessibility" section of the README."""
import os
import subprocess
import sys
import tempfile
import time
import urllib.request

ROOT = os.path.join(os.path.dirname(os.path.abspath(__file__)), "..")
PORT = int(os.environ.get("A11Y_PORT", "8791"))
BASE = f"http://127.0.0.1:{PORT}"
ADMIN = "audit-admin@example.com"

PUBLIC = ["/", "/login", "/signup", "/forgot", "/privacy", "/plan/beginner", "/plan/beginner/day/3"]
LEARNER = ["/dashboard", "/level", "/goal", "/day/1", "/day/1/q/1", "/day/1/q/4", "/day/1/q/5", "/profile", "/review",
           "/account", "/invite", "/leaderboard", "/certificate"]
ADMIN_PAGES = ["/admin", "/admin/users", "/admin/users/1", "/admin/reports", "/admin/questions", "/admin/activity",
               "/admin/system"]


def main() -> int:
    from axe_playwright_python.sync_playwright import Axe
    from playwright.sync_api import sync_playwright

    db_dir = tempfile.mkdtemp()
    env = dict(os.environ, DB_PATH=os.path.join(db_dir, "a11y.db"), ADMIN_EMAILS=ADMIN, SANDBOX="off",
               PYTHONPATH=ROOT)  # no mail settings: accounts are confirmed automatically
    env.pop("DATABASE_URL", None)
    server = subprocess.Popen([sys.executable, "-m", "uvicorn", "app.main:app", "--port", str(PORT)], cwd=ROOT, env=env,
                              stdout=subprocess.DEVNULL, stderr=subprocess.DEVNULL)
    try:
        for _ in range(60):
            try:
                urllib.request.urlopen(BASE + "/health", timeout=2)
                break
            except Exception:
                time.sleep(0.5)
        found = {}
        axe = Axe()
        with sync_playwright() as p:
            browser = p.chromium.launch(channel="chrome")
            for scheme in ("light", "dark"):
                ctx = browser.new_context(viewport={"width": 1100, "height": 800}, color_scheme=scheme)
                page = ctx.new_page()
                # one account that is both a learner and an admin: sign up the first time, log in after that
                page.goto(BASE + ("/signup" if scheme == "light" else "/login"))
                page.fill("input[name=email]", ADMIN)
                page.fill("input[name=password]", "password123")
                page.click("button.primary")
                if scheme == "light":
                    page.goto(BASE + "/level")
                    page.click("button[value=beginner]")
                    page.click("button[value='1']")
                for path in PUBLIC + LEARNER + ADMIN_PAGES:
                    page.goto(BASE + path, wait_until="load")
                    page.wait_for_timeout(150)
                    result = axe.run(page, options={"runOnly": {"type": "tag", "values": ["wcag2a", "wcag2aa", "wcag21aa", "best-practice"]}})
                    for v in result.response["violations"]:
                        key = (scheme, path, v["id"])
                        found[key] = (v["impact"], v["help"], [n["target"][0] for n in v["nodes"]][:3], len(v["nodes"]))
                ctx.close()
            browser.close()
    finally:
        server.terminate()
    if not found:
        print("No automated accessibility violations found.")
        return 0
    by_rule = {}
    for (scheme, path, rule), (impact, help_, targets, n) in found.items():
        by_rule.setdefault(rule, {"impact": impact, "help": help_, "where": []})["where"].append((scheme, path, n, targets))
    for rule, info in sorted(by_rule.items(), key=lambda kv: kv[1]["impact"] or ""):
        print(f"\n[{info['impact']}] {rule}: {info['help']}")
        for scheme, path, n, targets in info["where"][:6]:
            print(f"   {scheme:5} {path:22} x{n}  e.g. {targets[0] if targets else ''}")
        if len(info["where"]) > 6:
            print(f"   ... and {len(info['where']) - 6} more pages")
    print(f"\n{len(by_rule)} rule(s) violated on {len({k[1] for k in found})} page(s).")
    return 1


if __name__ == "__main__":
    sys.exit(main())
