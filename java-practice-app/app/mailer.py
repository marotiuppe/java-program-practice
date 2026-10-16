"""Sends plain-text email through Gmail SMTP (or any SMTP server).

Configure with environment variables (never commit them):
  SMTP_USER      the Gmail address that sends the mail
  SMTP_PASSWORD  a Gmail *app password* (Google Account -> Security -> 2-Step Verification -> App passwords)
  MAIL_FROM      optional display sender, defaults to SMTP_USER
  SMTP_HOST / SMTP_PORT  default smtp.gmail.com / 465 (SSL)
Set MAIL_BACKEND=memory in tests to collect messages in `outbox` instead of sending.
A personal Gmail account allows roughly 500 messages a day, which is plenty for verification and reset mail."""
import logging
import os
import smtplib
import ssl
from email.message import EmailMessage

log = logging.getLogger("mailer")

SMTP_HOST = os.environ.get("SMTP_HOST", "smtp.gmail.com")
SMTP_PORT = int(os.environ.get("SMTP_PORT", "465"))
SMTP_USER = os.environ.get("SMTP_USER", "")
SMTP_PASSWORD = os.environ.get("SMTP_PASSWORD", "")
MAIL_FROM = os.environ.get("MAIL_FROM", "") or SMTP_USER
MEMORY = os.environ.get("MAIL_BACKEND") == "memory"

outbox = []  # only used when MAIL_BACKEND=memory


def configured() -> bool:
    return MEMORY or bool(SMTP_USER and SMTP_PASSWORD)


def send(to: str, subject: str, body: str, headers: dict | None = None) -> bool:
    """Send one message. Blocking: call it from a background task. Returns False (and logs) on failure."""
    if MEMORY:
        outbox.append({"to": to, "subject": subject, "body": body, "headers": headers or {}})
        return True
    if not configured():
        log.warning("mail not configured; would have sent %r to %s", subject, to)
        return False
    msg = EmailMessage()
    msg["From"] = MAIL_FROM
    msg["To"] = to
    msg["Subject"] = subject
    for name, value in (headers or {}).items():
        msg[name] = value
    msg.set_content(body)
    try:
        with smtplib.SMTP_SSL(SMTP_HOST, SMTP_PORT, context=ssl.create_default_context(), timeout=20) as s:
            s.login(SMTP_USER, SMTP_PASSWORD)
            s.send_message(msg)
        return True
    except Exception:
        log.exception("could not send %r to %s", subject, to)
        return False
