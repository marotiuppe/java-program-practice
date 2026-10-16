# Java Practice App — Product Roadmap

*Last updated: 2026-10-16. Owner: project owner. Status: living document — tick tasks off and revise as we learn.*

**Goal:** a free, Java-only practice site where learners sign up, pick a level, and work through a 30-day plan (150 questions per level) with a real editor that compiles and runs Java 17 — and **want to come back every day**, **bring friends**, and **use it comfortably on a phone**.

**How to read this file:** Section 1 is where we are. Section 2 is what the research says. Section 3 is the engagement design. Section 4 is the phased task list (the part to work from). Sections 5–8 cover metrics, risks, decisions, and sources.

Effort sizes assume AI-assisted development: **S** ≈ under a day, **M** ≈ 2–4 days, **L** ≈ 1–2 weeks.

---

## 1. Where we are today

**Built and deployed**
- Sign-up / login, level selection (Beginner, Intermediate, Advanced), 30-day dashboard, per-question pages.
- Lightweight VS Code-style editor: syntax colors, auto-indent, bracket auto-close, autocomplete (keywords, types, snippets, member suggestions).
- Compile and run Java in the browser (stdin, compile errors, time limit, compile cache).
- **Run samples** against the examples in each question, hidden-test support, **Debug checks**, **Predict-the-output** answer check.
- Practice streak and dashboard stats; Day 30 mock-assessment timer.
- Postgres via `DATABASE_URL`; Render free plan with a self-ping every 5 minutes.

**Known gaps (these drive the roadmap)**
| Gap | Why it matters |
|---|---|
| Only 2 questions have hidden tests / Debug checks (about 417 could: 267 coding + 150 Debug) | Most grading is still "pass the printed samples" or self-marked |
| Visible sample outputs were hand-checked, not run against reference code (~110 spot-checked) | AI-written content; one broken snippet found in 90 checked |
| No password reset, no email verification, no per-IP rate limiting | Cannot safely open sign-up widely |
| User code can reach the network | Sandbox is basic (limits, unprivileged user, scrubbed environment) |
| Layout is responsive but not designed for phones | The likely main audience is on mobile |
| Nothing brings a learner back after a day away | Streak only *records*; nothing *reminds* or *rewards* |
| No analytics, no error reporting from users | We are flying blind once real users arrive |
| Free-plan constraints (sleep, cold start, one Java run at a time) | Affects first impression and concurrency |

---

## 2. Research findings (what to learn from)

> Confidence notes are included because several sources are blogs, trade press, or vendor pages rather than controlled studies.

### 2.1 Streaks and gamification work — but can backfire
- Duolingo's internal analysis found users who reached a **10-day streak** were less likely to drop off; the team admits this was affected by correlation and selection bias, but it justified heavy investment. Features that followed: streak-saver notifications, calendar view, **streak freezes**, rewards. A third-party newsletter attributes a 40%+ drop in daily churn and 450% DAU growth to years of streak work — *treat those numbers as unverified.* ([Pulse 2.0](https://pulse2.com/duolingo-daily-active-users-reach-58-7-million-as-retention-hits-record-84-and-social-accounts-top-1-billion-organic-impressions/), [Business of Apps](https://www.businessofapps.com/news/duolingos-streak-feature-drives-record-17-million-daus), [Lenny's Podcast summary](https://www.recall.it/summary/lennys-podcast/behind-the-product-duolingo-streaks-or-jackson-shuttleworth-group-pm-retention-team))
- **Counter-example:** making the streak easier to extend (only one exercise) did *not* raise daily actives — it captured the least engaged users. The streak's unit of work has to feel meaningful.
- A natural experiment on GitHub (ICSE 2021) found that removing public streak counters changed developer behaviour, and that streaking spreads through social influence. The authors warn gamification can push behaviour in unintended directions and sometimes lowers engagement. ([paper](https://2021.icse-conferences.org/details/icse-2021-papers/15/How-Gamification-Affects-Software-Developers-Cautionary-Evidence-from-a-Natural-Expe), [arXiv](https://ar5iv.labs.arxiv.org/html/2006.02371))
- **Takeaway:** keep the streak, add forgiveness (freezes), make the daily unit meaningful (one real question), and avoid shaming or pure points-chasing.

### 2.2 What comparable platforms do
- **LeetCode:** the default for algorithm interview prep; weaker for beginners and for "assessment-style" or project practice. **HackerRank:** guided paths, and many employers use it for screening. **Exercism:** free human mentoring is its differentiator, but its exercise count is smaller. **Codewars:** ranking system and gamification, isolated exercises. ([Scrimba comparison](https://scrimba.com/articles/best-coding-practice-platforms-and-challenge-websites-in-2026/), [DesignGurus](https://www.designgurus.io/answers/detail/which-platform-is-best-for-coding-practice))
- *I found no usable comparison of CodingBat or JetBrains Academy; study them directly before copying ideas.*
- **Our angle:** a **structured, day-by-day Java-only plan** with five *different kinds* of question each day (scenario, interview, assessment-style, predict-the-output, debug). That mix is not what the big platforms offer. Lean into it: "30 days to Java interview-ready".

### 2.3 Spaced repetition is a good fit for practice problems
- Practitioner writing argues that the *gaps* between repetitions matter more than the number of repetitions; re-solving a problem after days (not minutes) builds long-term recall. Tools exist that schedule problem reviews (e.g. a Forgot / Hard / Good / Easy rating). ([Red-Green-Code](https://www.redgreencode.com/leetcode-tip-9-spaced-repetition/))
- *Evidence here is practitioner experience, not controlled studies.* Still cheap to build and aligns with "bring learners back".

### 2.4 Mobile: a PWA is the right first step
- An installable web app needs **HTTPS, a web app manifest, and a service worker**. We already have HTTPS on Render. ([Twilio guide](https://www.twilio.com/blog/installable-web-apps-practical-introduction-progressive-web-apps), [SoluteLabs](https://solutelabs.com/blog/kick-starting-with-progressive-web-apps))
- Mobile coding tools converge on the same pattern: an **extra row of symbol keys** (brackets, `;`, Tab, arrows) so users do not hunt through keyboard screens, with rarer symbols behind long-press. ([Dev Keyboard](https://mwm.ai/apps/dev-keyboard-code-symbols/6777195040), [CodeBoard](https://www.apkmirror.com/apk/gazlaws/codeboard-keyboard-for-coding))
- *To verify before building:* web-push support and install behaviour differ between Android and iOS; test on real devices.

### 2.5 Growth: shareable proof beats ads
- A **public, verifiable, easy-to-share certificate** is something learners are proud to post, so it recruits new learners at no ongoing cost; word of mouth is far cheaper than paid acquisition; referral rewards (Alison gives free certificates for referrals) scale with the number of referrals. LinkedIn has made one-click sharing harder, so also offer a downloadable image and a copy-link. ([Accredible](https://www.accredible.com/blog/tips-for-increasing-word-of-mouth-program-referrals), [growth loop piece](https://wbcomdesigns.com/growth-loop-course-community/))
- *My searches found no solid source on SEO tactics for learning sites; plan SEO as an experiment and measure it.*

### 2.6 Platform constraints (Render free plan, per Render docs)
- Free web services **spin down after 15 minutes without inbound traffic** and take about a minute to wake. **750 free instance hours** per month; free Postgres **expires 30 days after creation** (14-day grace); **ephemeral filesystem**; free services cannot receive private-network traffic. Upgrading the workspace plan does **not** lift free-instance limits — only moving a service to a paid instance does. ([Render docs](https://render.com/docs/free), [Render pricing explained](https://livemy.app/blog/render-pricing))
- **Implication:** our database must stay on a non-expiring Postgres (Neon or similar). Past a few hundred active users, a paid instance (or a separate runner service) will be needed.

### 2.7 Safe code execution
- Common online-judge pattern is **layered**: no-network container, read-only filesystem, CPU/memory/time limits, syscall filtering (seccomp), namespaces, and often gVisor; the judge host should be isolated so an escape has limited blast radius. ([System-design write-up](https://www.techinterview.org/post/3233465013/system-design-online-judge/?format=md), [code-execution sandbox design](https://www.techinterview.org/post/3233472033/lld-code-execution-sandbox/))
- *These are practitioner write-ups; read the gVisor and seccomp docs before implementing.* On Render we cannot use gVisor or Docker-in-Docker, so the realistic options are: separate runner service with no secrets, plus Java-level and OS-level limits.

### 2.8 Account security basics
- Password reset: short-lived single-use tokens (roughly 15–60 minutes), rate limit by **both IP and email**, generic responses that do not reveal whether an account exists, notify the user of reset requests, log attempts. Lockouts can lock out real users — prefer throttling. ([captcha.eu](https://www.captcha.eu/?p=3747), [WSTG](https://pentest.y-security.de/Web%20Security%20Testing%20Guide/2022/04-Authentication_Testing/09-Testing_for_Weak_Password_Change_or_Reset_Functionalities)). *Check the OWASP cheat sheets directly for authoritative thresholds.*

---

## 3. Engagement design principles

1. **First win in under 2 minutes.** A new user should run a program and see output before they finish reading the dashboard.
2. **One clear next step.** Every screen answers "what do I do now?" — a **Continue** button that opens the exact next unsolved question.
3. **Meaningful daily goal.** Default: *solve 1 question today* (not "open the app"). Let users raise it to 3 or 5.
4. **Forgiveness over punishment.** Streak freezes (earned), a "repair" day, and a gentle tone. No shaming copy.
5. **Visible progress.** Rings, a calendar heatmap, a "Day complete" celebration, level certificates.
6. **Social, but opt-in.** Handles and leaderboards are optional; no real names or emails shown.
7. **Respect the learner.** No dark patterns, no spam reminders; every reminder has an easy off switch.
8. **Honest content.** Questions are AI-written — make errors easy to report and fix quickly.

---

## 4. Roadmap — phases and tasks

Each task lists an **outcome** you can check. Phases are ordered by value and dependency; phases 1 and 2 can overlap.

### Phase 0 — Stabilize the live site *(1 week)*
Goal: know that it works, and hear about problems fast.

- [ ] **0.1 Live smoke test (S).** Sign up, run Java, Run samples, Predict, redeploy and confirm data survives. *Outcome:* checklist in the README, all green on the deployed URL (JDK 21 with `--release 17`).
- [x] **0.2 "Report an issue" button (S).** On every question: category (wrong sample / unclear / broken code / other), optional note, auto-attach question id and the learner's code. Store in a `reports` table. *Outcome:* reports appear in the database.
- [x] **0.3 Admin page (M).** Protected by an admin flag: user count, daily active users, reports list with "resolved", hardest questions (most attempts, lowest solve rate). *Outcome:* one page that answers "how is it going?"
- [x] **0.4 Event logging (S).** Lightweight `events` table (signup, first_run, run, samples_pass, day_complete, return_visit). *Outcome:* the metrics in section 5 can be computed.
- [x] **0.5 Error monitoring (S).** Structured logs for 500s and runner failures; optionally a free error tracker. *Outcome:* a failed run leaves a trace you can find.
- [ ] **0.6 Continuous checks (M).** GitHub Action on every push: run `validate_tests.py`, the Predict snippet check, and a smoke test. *Outcome:* a broken question or build fails the push.
- [ ] **0.7 Database backup note (S).** Document the Neon backup/restore steps and test a restore once. *Outcome:* a written, tested recovery path.

### Phase 1 — Mobile-first experience *(1–2 weeks)*
Goal: practising on a phone feels natural. This should come early — the audience likely studies on phones.

- [ ] **1.1 Responsive redesign (M).** On narrow screens switch the question page to **tabs: Problem · Code · Output**, a sticky bottom action bar (Run, Samples, Save), larger tap targets (≥44px), and inputs at 16px+ so iOS does not zoom. *Outcome:* usable one-handed on a 360px-wide screen; no horizontal scroll.
- [ ] **1.2 Symbol toolbar for the editor (M).** A row above the keyboard: `Tab ( ) { } [ ] ; " . = < > ! & | ,` plus ← → ↑ ↓ and undo. Show only on touch devices. *Outcome:* a program can be typed without leaving the letter keyboard.
- [ ] **1.3 Keyboard-aware layout (S).** Use the visual-viewport API so the editor and autocomplete popup stay visible above the on-screen keyboard. *Outcome:* the caret line is never hidden.
- [ ] **1.4 Mobile autocomplete (S).** Popup appears above the caret on small screens, tap to accept. *Outcome:* suggestions work by touch.
- [ ] **1.5 PWA install (M).** Web app manifest, icons, theme color, service worker that caches the app shell and question pages; "Add to home screen" prompt after the first solved question. *Outcome:* installs on Android and iOS and opens full-screen; question text readable offline (running code still needs the server).
- [ ] **1.6 Cold-start experience (S).** Friendly "waking the server" screen with a countdown and retry, since free instances sleep. *Outcome:* a first visitor after idle sees progress, not a blank page.
- [ ] **1.7 Real-device test pass (S).** Test on at least one low-end Android phone, one iPhone, and Chrome/Safari/Firefox desktop. *Outcome:* a short compatibility table in the repo.

### Phase 2 — Engagement loop *(2–3 weeks)*
Goal: give people a reason to return tomorrow, and the day after.

- [x] **2.1 "Continue where you left off" (S).** Big button on the dashboard and home page → next unsolved question. *Outcome:* one click from login to coding.
- [ ] **2.2 Onboarding (M).** After sign-up: choose level (with a 5-question quick placement check as an option), set a daily goal (1/3/5 questions), then land directly on a pre-filled Day 1 Q1 with a guided "press Run" hint. *Outcome:* median time to first run under 2 minutes (measure with event 0.4).
- [ ] **2.3 Daily goal ring and "Day complete" moment (M).** Progress ring on the dashboard; a short celebration when all 5 questions of a day are solved, with a "share" button and a "tomorrow's day is ready" teaser. *Outcome:* visible daily finish line.
- [ ] **2.4 Streak v2 (M).** Calendar heatmap, **streak freeze** (earn one per 7-day streak, max 2 banked), "repair yesterday" once per month, longest-streak badge. Use the user's timezone (currently fixed by `APP_TIMEZONE`). *Outcome:* a missed day no longer wipes motivation.
- [ ] **2.5 XP, levels and badges (M).** Small, honest set: First Run, First Solve, 7-Day Streak, Day Complete, Week Complete, Level Complete, Debug Detective (10 Debug solved), Predictor (10 Predict correct). *Outcome:* a badge shelf on the profile.
- [ ] **2.6 Review queue — spaced repetition (M).** Questions solved with many attempts, revealed, or abandoned come back after 1, 3, 7, 21 days in a "Review" tab; learner rates Forgot / Hard / Good / Easy. *Outcome:* a daily "review 3 questions" option.
- [ ] **2.7 Reminders (M–L).** Opt-in: email reminder and weekly recap, and web-push on installed PWAs. Requires an email sender (see decision D2). Hard limits: at most one reminder a day, one-click unsubscribe. *Outcome:* lapsed users get a single, helpful nudge.
- [ ] **2.8 Hints ladder (M).** Optional, after N failed attempts: concept hint → approach hint → solution outline (your original plan said questions only, so make this **opt-in per user**). Mark questions where a hint was used. *Outcome:* fewer people quit on a stuck question.
- [ ] **2.9 Smarter compile-error help (M).** Plain-language explanation under common javac errors (missing semicolon, cannot find symbol, incompatible types). Rule-based first; optional AI-based later (see Phase 6). *Outcome:* beginners stay unblocked.

### Phase 3 — Content quality and full auto-grading *(2–4 weeks, uses agents)*
Goal: every question is correct, and grading is trustworthy.

- [ ] **3.1 Pilot on Beginner (L).** Agents write reference solutions + hidden tests for ~90 coding questions and Debug checks for 30; `tools/validate_tests.py` must pass. *Outcome:* Beginner fully verified; any wrong sample outputs corrected in the workbook.
- [ ] **3.2 Intermediate, then Advanced (L each).** Same method. *Outcome:* all ~416 questions with checks verified.
- [ ] **3.3 Re-verify Predict outputs (S).** Run the 90 snippets in CI (already scripted) and fail on non-determinism. *Outcome:* no flaky "Check answer".
- [ ] **3.4 Question feedback (S).** 👍/👎 and "too easy / too hard" per question. *Outcome:* data to tune difficulty.
- [ ] **3.5 Difficulty review (M).** Use solve-rate and attempts (0.3) to re-order or rewrite outliers. *Outcome:* an evidence-based curve.
- [ ] **3.6 Interview mode (M).** Timed set of 3 mixed questions from completed days, with a score and breakdown. *Outcome:* a reason to return after the 30 days.

### Phase 4 — Social features and growth *(2–3 weeks)*
Goal: users bring other users. Research suggests shareable proof and referrals are the cheapest channels.

- [ ] **4.1 Public profile page (M).** `/u/<handle>`: heatmap, badges, levels completed. Off by default; user opts in and chooses a handle. *Outcome:* a link people are happy to share.
- [ ] **4.2 Shareable progress cards (M).** Auto-generated image (e.g. "Day 12 complete · 12-day streak") with Open Graph tags so links preview well on WhatsApp, X, and LinkedIn; a copy-link and a download button. *Outcome:* every milestone has a one-tap share.
- [ ] **4.3 Level certificate (M).** On completing a level: a downloadable certificate with a public verification URL (`/verify/<id>`), plus LinkedIn "add to profile" instructions. *Outcome:* a proud, shareable end goal. Wording must be honest: "completed the practice plan", not an accredited qualification.
- [ ] **4.4 Referral (M).** Personal invite link; both people earn a streak freeze when the invitee solves their first question. *Outcome:* measurable invites → activated users.
- [ ] **4.5 Weekly leaderboard (M).** Opt-in, by handle, ranked by questions solved that week with level filters; resets every Monday so newcomers can win. *Outcome:* friendly competition without a permanent top-10 lock-in.
- [ ] **4.6 Cohorts / "batches" (L).** Join with a code (class, college, friends); cohort page with members' streaks and a weekly challenge. *Outcome:* group accountability.
- [ ] **4.7 Teacher / institute mode (L).** A teacher creates a class, shares an invite code, sees each student's progress per day, and can assign a start date. *Outcome:* a channel for institutes and colleges — potentially large user batches per signup.
- [ ] **4.8 Public landing and SEO (M).** Landing page, "30-day Java plan" pages per level, public (read-only) day previews, sitemap, fast load. Treat SEO as an experiment; measure organic sign-ups for 8 weeks. *Outcome:* visitors can understand the product before signing up.
- [ ] **4.9 Community loops (S, ongoing).** Weekly "question of the week", a short changelog page, and a feedback/feature-vote page. *Outcome:* users see the product improving.

### Phase 5 — Security, reliability and scale *(2–3 weeks; do 5.1–5.4 before any public launch)*
- [ ] **5.1 Rate limiting and abuse control (M).** Per-IP and per-email limits on sign-up, login, reset, and code runs; a global cap on concurrent runs with a visible queue; optional CAPTCHA after repeated failures. *Outcome:* a bot cannot exhaust the runner.
- [ ] **5.2 Email verification + password reset (M).** Short-lived single-use tokens, generic responses, notify on reset. Requires an email sender (D2). *Outcome:* recoverable accounts.
- [ ] **5.3 Sandbox hardening (L).** (a) Move the runner into a **separate service with no database credentials**; (b) block outbound network for user code (test in the Docker image; evaluate options available on the host); (c) add Java-level and process-level limits; (d) review file/process permissions. *Outcome:* user code cannot reach the network or secrets. *Be upfront in docs about what is and is not isolated.*
- [ ] **5.4 Privacy and account control (S–M).** Privacy page, account deletion, data export. *Outcome:* users control their data.
- [ ] **5.5 Hosting plan (M).** Decide when to leave the free plan (see §6). Move the runner and web app to paid instances if daily actives pass the threshold you set. *Outcome:* no "server is busy" under load.
- [ ] **5.6 Performance (S–M).** Pre-warm common questions' compile cache; trim page weight; cache rendered question HTML. *Outcome:* snappy pages on slow phones.
- [ ] **5.7 Accessibility pass (M).** Keyboard navigation, contrast, screen-reader labels, reduced-motion setting. *Outcome:* usable by more learners.

### Phase 6 — Ideas to evaluate later
- [ ] **AI tutor (L, paid API).** "Explain my error", "review my solution", hint generation. Needs an outside API and ongoing cost — only if you drop the "no outside APIs" rule (D3).
- [ ] **Project tracks (L).** Small multi-file projects (bank system, library manager) beyond single-file questions; needs a multi-file editor.
- [ ] **More curated paths (M).** "Collections in 7 days", "OOP drills", "Interview sprint".
- [ ] **Native app wrapper (L).** Only if the PWA is not enough; a Capacitor/TWA wrapper over the same site.
- [ ] **Localization (M).** UI and explanations in additional languages.
- [ ] **Multi-language support (L).** Out of scope for now (Java only).

---

## 5. Metrics to track (start with Phase 0.4)

| Metric | Why | Target to aim for (adjust once we have a baseline) |
|---|---|---|
| Sign-up → first run, within the same session | Is onboarding working? | Majority within 2 minutes |
| Day-1 / Day-7 / Day-30 retention | Core health | Track trend, not a fixed number |
| Questions solved per active user per week | Depth of use | Rising |
| Streak distribution (1, 3, 7, 14, 30 days) | Does the loop work? | Healthy tail at 7+ |
| Day-complete rate; level-complete rate | Plan effectiveness | Rising |
| Run success rate; runner timeouts / "busy" rate | Reliability | Busy rate near 0 |
| Reports per 100 solves | Content quality | Falling |
| Invites sent / invitees activated | Growth loop | Positive and rising |
| Share-card clicks → sign-ups | Growth loop | Positive and rising |

Rule of thumb: instrument each new feature with an event, and compare before/after rather than guessing.

---

## 6. Risks and constraints

| Risk | Mitigation |
|---|---|
| Free-plan sleep/cold start hurts first impressions | Waking screen (1.6); self-ping already present; move to a paid instance once usage justifies it |
| Free instance hours (750/month) are shared across all free services in the workspace | Keep this the only free service, or upgrade |
| One Java run at a time on 512 MB | Queue with visible position; compile cache; paid instance later |
| Untrusted code can still use the network | Phase 5.3; do not publicize sign-up widely until done |
| AI-written content has errors | Report button, validator, CI, hidden tests |
| Over-gamification harms intrinsic motivation (research caution) | Opt-in social features, forgiving streaks, no shaming |
| Email deliverability and cost | Start with a small, well-reputed sender; keep volume low; one-click unsubscribe |
| Data loss if the DB expires or is lost | Non-expiring Postgres, tested restore (0.7) |
| Certificates misread as formal qualifications | Clear wording on every certificate |

---

## 7. Decisions needed from the owner

- **D1 — Audience and launch:** friends and a class only, or a public launch? (Decides whether Phase 5.1–5.4 must be done first.)
- **D2 — Email:** allowed to use an outside email sender (needed for verification, reset, reminders)? If not, reminders are limited to web-push and in-app.
- **D3 — Outside APIs:** keep "no outside APIs", or allow an AI tutor in Phase 6?
- **D4 — Hints and solutions:** keep "questions only" for everyone, or offer opt-in hints and solutions (2.8)?
- **D5 — Budget:** when a paid instance is acceptable, and for what (web, runner, database).
- **D6 — Branding:** name, logo, and a domain, needed for sharing cards, certificates and SEO.

---

## 8. Suggested next steps (first two weeks)

1. **Phase 0** items 0.1, 0.2, 0.4 (smoke test, report button, events).
2. **2.1 Continue button** and **2.2 onboarding** (small effort, big first-impression effect).
3. **Phase 1.1–1.3** (responsive layout, symbol toolbar, keyboard-aware editor).
4. Start the **Beginner pilot in 3.1** in parallel with agents, since it takes the longest.
5. Decide D1 and D2 so Phases 2.7 and 5.2 are unblocked.

---

## Sources

- Duolingo streaks: [Pulse 2.0](https://pulse2.com/duolingo-daily-active-users-reach-58-7-million-as-retention-hits-record-84-and-social-accounts-top-1-billion-organic-impressions/) · [Business of Apps](https://www.businessofapps.com/news/duolingos-streak-feature-drives-record-17-million-daus) · [Lenny's Podcast summary](https://www.recall.it/summary/lennys-podcast/behind-the-product-duolingo-streaks-or-jackson-shuttleworth-group-pm-retention-team)
- Gamification risks: [ICSE 2021 GitHub streak study](https://2021.icse-conferences.org/details/icse-2021-papers/15/How-Gamification-Affects-Software-Developers-Cautionary-Evidence-from-a-Natural-Expe) · [arXiv version](https://ar5iv.labs.arxiv.org/html/2006.02371)
- Practice platforms: [Scrimba comparison](https://scrimba.com/articles/best-coding-practice-platforms-and-challenge-websites-in-2026/) · [DesignGurus](https://www.designgurus.io/answers/detail/which-platform-is-best-for-coding-practice)
- Spaced repetition: [Red-Green-Code](https://www.redgreencode.com/leetcode-tip-9-spaced-repetition/)
- PWA: [Twilio guide](https://www.twilio.com/blog/installable-web-apps-practical-introduction-progressive-web-apps) · [SoluteLabs](https://solutelabs.com/blog/kick-starting-with-progressive-web-apps)
- Mobile coding input: [Dev Keyboard](https://mwm.ai/apps/dev-keyboard-code-symbols/6777195040) · [CodeBoard](https://www.apkmirror.com/apk/gazlaws/codeboard-keyboard-for-coding)
- Growth and certificates: [Accredible](https://www.accredible.com/blog/tips-for-increasing-word-of-mouth-program-referrals) · [growth-loop article](https://wbcomdesigns.com/growth-loop-course-community/)
- Render free plan: [Render docs](https://render.com/docs/free) · [Render pricing explained](https://livemy.app/blog/render-pricing)
- Sandboxing: [Online-judge system design](https://www.techinterview.org/post/3233465013/system-design-online-judge/?format=md) · [Code-execution sandbox LLD](https://www.techinterview.org/post/3233472033/lld-code-execution-sandbox/)
- Account security: [captcha.eu on reset abuse](https://www.captcha.eu/?p=3747) · [WSTG weak reset testing](https://pentest.y-security.de/Web%20Security%20Testing%20Guide/2022/04-Authentication_Testing/09-Testing_for_Weak_Password_Change_or_Reset_Functionalities)
