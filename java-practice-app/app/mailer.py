"""Sends plain-text email. Two ways, chosen by environment variables (never commit them):

1. HTTP API (works on Render's FREE plan, which blocks SMTP ports): Brevo
     BREVO_API_KEY  an API key from Brevo (Settings -> SMTP & API -> API keys)
     MAIL_FROM      the sender address, which must be verified in Brevo (Senders, domains & dedicated IPs)
   Brevo's free plan allows about 300 emails a day.
2. SMTP (Gmail or any provider; needs a host that allows outbound SMTP, e.g. a paid Render instance or your own PC)
     SMTP_USER      the sending address (for Gmail: the Gmail address)
     SMTP_PASSWORD  for Gmail an *app password* (Google Account -> Security -> 2-Step Verification -> App passwords)
     MAIL_FROM      optional display sender, defaults to SMTP_USER
     SMTP_HOST / SMTP_PORT  default smtp.gmail.com / 465 (SSL)
If both are set, the HTTP API is used. Set MAIL_BACKEND=memory in tests to collect messages in `outbox`."""
import json
import logging
import os
import smtplib
import ssl
import urllib.error
import urllib.request
from email.message import EmailMessage
from email.utils import parseaddr

log = logging.getLogger("mailer")

SMTP_HOST = os.environ.get("SMTP_HOST", "smtp.gmail.com")
SMTP_PORT = int(os.environ.get("SMTP_PORT", "465"))
SMTP_USER = os.environ.get("SMTP_USER", "")
SMTP_PASSWORD = os.environ.get("SMTP_PASSWORD", "")
BREVO_API_KEY = os.environ.get("BREVO_API_KEY", "")
BREVO_URL = os.environ.get("BREVO_URL", "https://api.brevo.com/v3/smtp/email")
MAIL_FROM = os.environ.get("MAIL_FROM", "") or SMTP_USER
MEMORY = os.environ.get("MAIL_BACKEND") == "memory"

outbox = []  # only used when MAIL_BACKEND=memory
last_error = ""  # why the most recent send failed ("" if it worked); shown on the admin System page


def configured() -> bool:
    return MEMORY or bool(BREVO_API_KEY and MAIL_FROM) or bool(SMTP_USER and SMTP_PASSWORD)


def backend() -> str:
    """Short description of how mail is sent, for the admin System page."""
    if MEMORY:
        return "memory (test)"
    if BREVO_API_KEY and MAIL_FROM:
        return f"Brevo HTTP API, sending as {parseaddr(MAIL_FROM)[1]}"
    if SMTP_USER and SMTP_PASSWORD:
        note = " (this port is blocked on Render's free plan)" if SMTP_PORT in (25, 465, 587) else ""
        return f"SMTP {SMTP_HOST}:{SMTP_PORT} as {SMTP_USER}{note}"
    return "not configured"


def _send_brevo(to: str, subject: str, body: str, headers: dict | None, html: str | None = None) -> bool:
    name, address = parseaddr(MAIL_FROM)
    payload = {"sender": {"email": address, **({"name": name} if name else {})}, "to": [{"email": to}],
               "subject": subject, "textContent": body}
    if html:
        payload["htmlContent"] = html
    if headers:
        payload["headers"] = headers
    req = urllib.request.Request(
        BREVO_URL, data=json.dumps(payload).encode(), method="POST",
        headers={"api-key": BREVO_API_KEY, "content-type": "application/json", "accept": "application/json"})
    global last_error
    try:
        with urllib.request.urlopen(req, timeout=20) as r:
            ok = 200 <= r.status < 300
            last_error = "" if ok else f"Brevo answered HTTP {r.status}"
            return ok
    except urllib.error.HTTPError as exc:
        detail = exc.read()[:300].decode("utf-8", "replace")
        last_error = f"Brevo rejected the message: HTTP {exc.code} {detail}"
        log.error("Brevo rejected %r to %s: HTTP %s %s", subject, to, exc.code, detail)
    except Exception as exc:
        last_error = f"{type(exc).__name__}: {exc}"
        log.exception("could not send %r to %s through Brevo", subject, to)
    return False


def send(to: str, subject: str, body: str, headers: dict | None = None, html: str | None = None) -> bool:
    """Send one message (plain text `body`, plus an optional `html` version). Blocking: call it from a
    background task. Returns False (and logs) on failure."""
    if MEMORY:
        outbox.append({"to": to, "subject": subject, "body": body, "headers": headers or {}, "html": html})
        return True
    if not configured():
        log.warning("mail not configured; would have sent %r to %s", subject, to)
        return False
    if BREVO_API_KEY and MAIL_FROM:
        return _send_brevo(to, subject, body, headers, html)
    msg = EmailMessage()
    msg["From"] = MAIL_FROM
    msg["To"] = to
    msg["Subject"] = subject
    for name, value in (headers or {}).items():
        msg[name] = value
    msg.set_content(body)
    if html:
        msg.add_alternative(html, subtype="html")
    global last_error
    try:
        ctx = ssl.create_default_context()
        if SMTP_PORT == 465:  # implicit TLS
            conn = smtplib.SMTP_SSL(SMTP_HOST, SMTP_PORT, context=ctx, timeout=20)
        else:  # 587 / 2525 / ...: connect in plain text, then upgrade with STARTTLS before logging in
            conn = smtplib.SMTP(SMTP_HOST, SMTP_PORT, timeout=20)
            conn.ehlo()
            conn.starttls(context=ctx)
            conn.ehlo()
        with conn as s:
            s.login(SMTP_USER, SMTP_PASSWORD)
            s.send_message(msg)
        last_error = ""
        return True
    except Exception as exc:
        last_error = f"{type(exc).__name__}: {exc}"
        log.exception("could not send %r to %s", subject, to)
        return False
