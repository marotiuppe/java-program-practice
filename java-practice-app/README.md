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

`ADMIN_EMAILS` (comma-separated emails allowed to open `/admin`: user counts, event totals, issue reports, hardest questions).


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

## Email (verification and password reset)

Mail goes through Gmail SMTP. Set these environment variables on Render (never commit them):

- `SMTP_USER`: the Gmail address that sends the mail.
- `SMTP_PASSWORD`: a Gmail **app password** (Google Account → Security → 2-Step Verification → App passwords). Your normal password will not work.
- `MAIL_FROM` (optional sender name/address), `APP_BASE_URL` (the public site URL used in links; defaults to `RENDER_EXTERNAL_URL`).

Without `SMTP_USER`/`SMTP_PASSWORD`, new accounts are marked verified automatically and "Forgot password" says reset is not set up.
A personal Gmail account sends about 500 messages a day. If you outgrow that, switch `SMTP_HOST`/`SMTP_PORT` to another provider; no code change is needed.
