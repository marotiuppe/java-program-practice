"""SQLite storage. NOTE: on Render's free plan the filesystem is ephemeral, so this
file is wiped on every redeploy/restart. See README for the mitigation."""
import hashlib
import hmac
import os
import secrets
import sqlite3
import time
from contextlib import contextmanager

DB_PATH = os.environ.get("DB_PATH", os.path.join(os.path.dirname(__file__), "..", "data", "app.db"))

SCHEMA = """
CREATE TABLE IF NOT EXISTS users (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    email TEXT NOT NULL UNIQUE,
    pw_hash TEXT NOT NULL,
    level TEXT NOT NULL DEFAULT '',
    created_at INTEGER NOT NULL
);
CREATE TABLE IF NOT EXISTS progress (
    user_id INTEGER NOT NULL,
    level TEXT NOT NULL,
    day INTEGER NOT NULL,
    qnum INTEGER NOT NULL,
    status TEXT NOT NULL DEFAULT 'todo',
    code TEXT NOT NULL DEFAULT '',
    attempts INTEGER NOT NULL DEFAULT 0,
    updated_at INTEGER NOT NULL,
    PRIMARY KEY (user_id, level, day, qnum)
);
"""


def init() -> None:
    os.makedirs(os.path.dirname(os.path.abspath(DB_PATH)), exist_ok=True)
    with connect() as c:
        c.executescript(SCHEMA)


@contextmanager
def connect():
    conn = sqlite3.connect(DB_PATH, timeout=10)
    conn.row_factory = sqlite3.Row
    conn.execute("PRAGMA journal_mode=WAL")
    try:
        yield conn
        conn.commit()
    finally:
        conn.close()


def hash_password(password: str) -> str:
    salt = secrets.token_bytes(16)
    digest = hashlib.scrypt(password.encode(), salt=salt, n=2**14, r=8, p=1)
    return f"scrypt${salt.hex()}${digest.hex()}"


def verify_password(password: str, stored: str) -> bool:
    try:
        _, salt_hex, digest_hex = stored.split("$")
        digest = hashlib.scrypt(password.encode(), salt=bytes.fromhex(salt_hex), n=2**14, r=8, p=1)
        return hmac.compare_digest(digest.hex(), digest_hex)
    except Exception:
        return False


def create_user(email: str, password: str):
    try:
        with connect() as c:
            cur = c.execute(
                "INSERT INTO users (email, pw_hash, created_at) VALUES (?, ?, ?)",
                (email, hash_password(password), int(time.time())),
            )
            return cur.lastrowid
    except sqlite3.IntegrityError:
        return None


def get_user_by_email(email: str):
    with connect() as c:
        return c.execute("SELECT * FROM users WHERE email = ?", (email,)).fetchone()


def get_user(user_id: int):
    with connect() as c:
        return c.execute("SELECT * FROM users WHERE id = ?", (user_id,)).fetchone()


def set_level(user_id: int, level: str) -> None:
    with connect() as c:
        c.execute("UPDATE users SET level = ? WHERE id = ?", (level, user_id))


def get_progress(user_id: int, level: str):
    with connect() as c:
        return c.execute(
            "SELECT day, qnum, status FROM progress WHERE user_id = ? AND level = ?",
            (user_id, level),
        ).fetchall()


def get_question_progress(user_id: int, level: str, day: int, qnum: int):
    with connect() as c:
        return c.execute(
            "SELECT status, code, attempts FROM progress "
            "WHERE user_id = ? AND level = ? AND day = ? AND qnum = ?",
            (user_id, level, day, qnum),
        ).fetchone()


def save_progress(user_id, level, day, qnum, code, status, count_attempt):
    """Upsert. A status of None keeps the current one (default 'attempted' once code is saved)."""
    now = int(time.time())
    with connect() as c:
        row = c.execute(
            "SELECT status, attempts FROM progress "
            "WHERE user_id = ? AND level = ? AND day = ? AND qnum = ?",
            (user_id, level, day, qnum),
        ).fetchone()
        if row is None:
            c.execute(
                "INSERT INTO progress (user_id, level, day, qnum, status, code, attempts, updated_at) "
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                (user_id, level, day, qnum, status or "attempted", code, 1 if count_attempt else 0, now),
            )
        else:
            new_status = status or ("attempted" if row["status"] == "todo" else row["status"])
            c.execute(
                "UPDATE progress SET status = ?, code = ?, attempts = attempts + ?, updated_at = ? "
                "WHERE user_id = ? AND level = ? AND day = ? AND qnum = ?",
                (new_status, code, 1 if count_attempt else 0, now, user_id, level, day, qnum),
            )
