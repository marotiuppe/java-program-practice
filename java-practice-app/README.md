# Java Practice App

Sign up / log in, choose a level (Beginner, Intermediate, Advanced), work through a 30-day plan
(5 questions a day), write Java 17 in the browser, compile and run it, and track progress per day.

Stack: FastAPI + Jinja2 + a plain textarea editor, SQLite, JDK 21 in the Docker image (compiles with `--release 17`).
Questions come from `app/content/*.md` (copies of the three workbooks).

## Run locally

```
python -m venv .venv
.venv\Scripts\activate            # Windows (use source .venv/bin/activate on Linux/macOS)
pip install -r requirements.txt
set JAVA_BIN_DIR=C:\Program Files\Java\jdk-25.0.3\bin   # any JDK 17+ bin folder; omit if java/javac are on PATH
uvicorn app.main:app --reload --port 8000
```

Open http://localhost:8000. With JDK 17 or newer the app compiles with `--release 17`. With an older
JDK (for example JDK 8 on your PATH) the flag is skipped automatically, but Java 9+ syntax such as
`record`, `var`, text blocks and `List.of` will not compile. Point `JAVA_BIN_DIR` at a JDK 17+ for the
full question set (the Docker image uses JDK 21 with `--release 17`).

## Deploy on Render (free plan)

1. Push this folder to a Git repository.
2. Render dashboard: New > Blueprint (uses `render.yaml`) or New > Web Service > Docker, plan Free.
3. Render sets `RENDER_EXTERNAL_URL` automatically. The app then calls its own public
   `/health` every 5 minutes (`KEEPALIVE_SECONDS`) to avoid the free-plan idle sleep.

## Known limits on the free plan

- **Data loss without a database URL:** the filesystem is ephemeral, so the local SQLite file
  (`data/app.db`) is wiped on every redeploy and restart. Set `DATABASE_URL` (see below) to keep
  users and progress in Postgres instead. The editor also keeps a copy of each draft in the browser
  (`localStorage`) as a partial backup.
- **Self-ping is best effort.** It only works while the service is already awake. A redeploy or a
  Render restart still cold-starts it (30-60 s). Free instance hours are shared across all your
  free services in the workspace.
- **Sandbox is basic:** time/CPU/file/process limits, a temp dir, a scrubbed environment and an
  unprivileged uid (when started as root in Docker). It does not block network access from user code.
  Do not open sign-up to the public without a stronger sandbox.
- One Java run at a time (about 512 MB RAM); compile can take several seconds on the shared CPU.

## Configuration (environment variables)

`SECRET_KEY` (session signing, required in production), `DATABASE_URL` (Postgres connection string,
e.g. from Neon; when unset the app uses a local SQLite file at `DB_PATH`), `DB_PATH`, `JAVA_BIN_DIR`, `JAVAC_FLAGS`,
`JAVA_FLAGS`, `COMPILE_TIMEOUT`, `RUN_TIMEOUT`, `KEEPALIVE_URL`, `KEEPALIVE_SECONDS`, `RUNNER_UID`.

`ADMIN_EMAILS` (comma-separated emails of admins; the account must also have a confirmed email). Admins land on `/admin`, which has its own menu: Overview (key numbers, sign-up funnel, retention, charts), Users (search, and each learner's progress with account actions), Reports (issue reports), Questions (solve rates, attempts, hints and reports per question), Activity (event feed) and System (database, sandbox, email status).


## Live smoke test (run after every deploy)

Tick each item on the deployed URL. Use a throwaway account.

- [ ] `/health` returns `{"status":"ok"}`.
- [ ] Sign up with a new email; you land on the level page.
- [ ] Pick a level; the dashboard shows a "Start" button.
- [ ] Open Day 1 Q1, press **Run**; output appears (JDK 21 compiling with `--release 17`).
- [ ] Press **Run samples** on a question that has them; results show pass/fail per sample.
- [ ] On a Predict question, **Check answer** and **Reveal output** both respond.
- [ ] **Save**, reload the page; your code is still there.
- [ ] **Report an issue** sends; the report shows on `/admin` (if your email is in `ADMIN_EMAILS`).
- [ ] Redeploy (or restart) the service, log in again; the account, progress and code survived.

## Email (verification, password reset, reminders)

**Important:** Render's free plan blocks outbound SMTP (ports 25, 465, 587), so Gmail SMTP does **not** work there. Pick one:

1. **Brevo HTTP API (works on the free plan).** Create a free Brevo account, add and verify your sender address (Senders, domains & dedicated IPs), create an API key (Settings, SMTP & API), then set on Render: `BREVO_API_KEY` and `MAIL_FROM` (the verified sender, e.g. `Java Practice <you@example.com>`). About 300 emails a day are free. A sender on a free mailbox domain such as gmail.com may land in spam more often; a sender on your own domain with DKIM set up in Brevo is best.
2. **Gmail or any SMTP**, on a host that allows outbound SMTP (a paid Render instance, or your own machine): set `SMTP_USER` and `SMTP_PASSWORD` (for Gmail an **app password**: Google Account, Security, 2-Step Verification, App passwords), optionally `SMTP_HOST`/`SMTP_PORT` (default `smtp.gmail.com`/`465`) and `MAIL_FROM`.

Also set `APP_BASE_URL` to the public site URL (links in emails use it). If both options are set, the HTTP API is used. Without any of them, new accounts are marked verified automatically and "Forgot password" says reset is not set up. The admin System page shows which method is active.

## Sandbox: what is and is not isolated

Learner code runs in a separate process with: a fresh temp directory, wall-clock timeouts, CPU / file-size / process / open-file limits, a scrubbed environment, capped output, and an unprivileged user (uid 10001). The database folder is root-only, and secrets (such as `DATABASE_URL`) exist only in the web process, not in the learner's process.

On Linux a **seccomp filter** (`app/sandbox.py`) also stops learner code from opening network sockets (no outbound requests, DNS or listening servers) and blocks risky system calls. It is switched on at startup only after a self-test passes (a normal program still runs and a local connection is refused); the admin page shows the result. Set `SANDBOX=off` to disable it.

**Not isolated:** it is not a full container. Learner code can still read world-readable files, write to `/tmp`, and start processes (limited in number) as the unprivileged user. A separate runner service with its own host is not possible on the free plan, so the web app and the runner share one machine. Verify with `docker run --rm -v "$PWD/tools:/app/tools" java-practice-test python tools/check_sandbox.py` after building the image.

## Reminder emails

Opt-in only (account page, or the checkbox at onboarding). At most one email per user per day, sent after `REMINDER_HOUR` (default 18) in the user's timezone: a streak nudge, one note after 3 days away and one after 14, and a Sunday recap. Needs the email settings above plus `APP_BASE_URL` (so links point at your site). Optional: `REMINDER_HOUR`, `REMINDER_MAX_PER_RUN` (default 100), `REMINDER_CHECK_SECONDS` (default 1800). Sent mail is de-duplicated through the events table, so restarts never resend.

## Backups and restore

Learner data lives in Postgres (Neon). Two independent safety nets:

1. **Neon's own history.** In the Neon console, open your project, then *Branches* → *Restore*, and pick a point in time. The free plan keeps a short history window, so treat it as protection against a recent mistake, not a long-term backup.
2. **Your own copy, with this repo's tool.** From a machine that has the repo and Python:

   ```
   pip install -r requirements.txt
   DATABASE_URL="<your Neon connection string>" python tools/db_backup.py dump backup-YYYY-MM-DD.json
   ```

   Do this weekly (and before risky changes). The file contains password hashes and learners' code, so store it somewhere private, not in git.

**To restore** into a new, empty database (create a new Neon project or database first):

```
DATABASE_URL="<connection string of the EMPTY database>" python tools/db_backup.py restore backup-YYYY-MM-DD.json
```

Then point the Render service's `DATABASE_URL` at it and redeploy. The tool refuses to restore into a database that already has users. The dump and restore round trip was tested on SQLite and on a real Postgres 16 server (accounts, saved code, new sign-ups after restore all work); it has not yet been run against Neon itself, so do one trial restore into a spare Neon database before you rely on it.

## Automated checks

`.github/workflows/ci.yml` runs on every push: question tests against reference solutions, the 90 Predict snippets (twice each, to catch flaky output), compile-error help against real javac, the network sandbox, and a smoke test of the main flows. Run any of them locally from `java-practice-app/` with `python tools/<name>.py` (needs JDK 17+; `smoke_test.py` also accepts `SMOKE_DATABASE_URL` to test against a throwaway Postgres).

## Email design

Every email (confirm email, reset password, password changed, streak nudge, "we saved your place", weekly recap, admin test) is built in `app/email_templates.py` and `app/templates/email/layout.html`: logo, a banner image, one clear button, a footer with Privacy and (for reminders) Unsubscribe, a hidden preview line, a dark-mode version, and a plain-text copy sent alongside the HTML. Banner images and the logo live in `app/static/email/` and are loaded from `APP_BASE_URL`, so set that variable. To see every email in your browser run `python tools/preview_emails.py` (it writes HTML files to `email-preview/`). To change the artwork edit and re-run `python tools/make_email_images.py`.

## Accessibility

Built in: a "Skip to main content" link, labelled form fields and a labelled code editor, one top-level heading per page, proper tab roles with arrow-key navigation on the phone question layout, a focus-trapping celebration dialog that returns focus when it closes, announcements for run results, hints and messages, text contrast that passes WCAG AA in light and dark mode, and `prefers-reduced-motion` support. **Keyboard users:** inside the code editor Tab indents; press **Esc, then Tab** to move out (a hint under the editor says so).

`python tools/a11y_audit.py` runs axe-core over every main page in light and dark mode (also in CI). It finds roughly a third of real problems, so it is not proof of full accessibility. Not yet done: testing with real screen readers (NVDA, VoiceOver, TalkBack) and with browser zoom at 200 to 400 percent; both need a person.
