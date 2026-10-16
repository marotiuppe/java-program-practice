FROM python:3.12-slim

RUN apt-get update \
    && apt-get install -y --no-install-recommends openjdk-21-jdk-headless \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /app
COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

COPY app ./app
# Data dir is root-only: user programs run as uid 10001 and cannot read the database.
RUN mkdir -p /app/data && chmod 700 /app/data

ENV DB_PATH=/app/data/app.db \
    PYTHONUNBUFFERED=1

# Runs as root on purpose: runner.py drops each Java child process to uid 10001.
CMD ["sh", "-c", "uvicorn app.main:app --host 0.0.0.0 --port ${PORT:-10000}"]
