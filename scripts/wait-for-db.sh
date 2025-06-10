#!/usr/bin/env bash
set -euo pipefail

: "${DB_HOST:?DB_HOST is required}"
: "${DB_PORT:?DB_PORT is required}"
: "${DB_USER:?DB_USER is required}"
: "${DB_PASSWORD:?DB_PASSWORD is required}"
: "${DB_NAME:?DB_NAME is required}"

host=${DB_HOST}
port=${DB_PORT}
user=${DB_USER}
database=${DB_NAME}
export PGPASSWORD=${DB_PASSWORD}

for i in {1..60}; do
  if pg_isready -h "$host" -p "$port" -U "$user" >/dev/null 2>&1; then
    if psql -h "$host" -p "$port" -U "$user" -d "$database" -c 'SELECT 1' >/dev/null 2>&1; then
      exec "$@"
    fi
  fi
  echo "Waiting for $host:$port..."
  sleep 1
done
echo "Timeout waiting for $host:$port" >&2
exit 1
