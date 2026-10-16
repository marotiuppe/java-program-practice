"""Back up and restore the whole database as one JSON file (works on SQLite and Postgres).

    python tools/db_backup.py dump backup.json          # uses DATABASE_URL (Postgres) or DB_PATH (SQLite)
    python tools/db_backup.py restore backup.json       # into an EMPTY database; refuses if users already exist

The backup contains password hashes and learner code: keep it private. For Neon see the README
("Backups and restore")."""
import json
import os
import sys

sys.path.insert(0, os.path.join(os.path.dirname(os.path.abspath(__file__)), ".."))
from app import db  # noqa: E402

TABLES = ["users", "progress", "activity", "events", "reviews", "reports", "auth_tokens"]
IDENTITY = {"users": "id", "events": "id", "reports": "id"}  # tables with an auto-increment id


def dump(path: str) -> None:
    db.init()
    data = {"format": 1, "tables": {}}
    with db.connect() as c:
        for t in TABLES:
            data["tables"][t] = [dict(r) for r in db._x(c, f"SELECT * FROM {t}").fetchall()]
    with open(path, "w", encoding="utf-8") as f:
        json.dump(data, f)
    print("backed up:", ", ".join(f"{t}={len(rows)}" for t, rows in data["tables"].items()))


def restore(path: str) -> None:
    with open(path, encoding="utf-8") as f:
        data = json.load(f)
    db.init()
    with db.connect() as c:
        if db._x(c, "SELECT COUNT(*) AS n FROM users").fetchone()["n"]:
            sys.exit("refusing to restore: the target database already has users")
        for t in TABLES:
            for row in data["tables"].get(t, []):
                cols = list(row)
                db._x(c, f"INSERT INTO {t} ({', '.join(cols)}) VALUES ({', '.join('?' for _ in cols)})",
                      [row[k] for k in cols])
        if db.PG:  # explicit ids do not advance Postgres identity counters; do it by hand
            for t, col in IDENTITY.items():
                c.execute(f"SELECT setval(pg_get_serial_sequence('{t}', '{col}'), "
                          f"COALESCE((SELECT MAX({col}) FROM {t}), 1), (SELECT COUNT(*) FROM {t}) > 0)")
    print("restored:", ", ".join(f"{t}={len(data['tables'].get(t, []))}" for t in TABLES))


if __name__ == "__main__":
    if len(sys.argv) != 3 or sys.argv[1] not in ("dump", "restore"):
        sys.exit(__doc__)
    (dump if sys.argv[1] == "dump" else restore)(sys.argv[2])
