#!/usr/bin/env bash
set -e
host=${DB_HOST:-db}
port=${DB_PORT:-5432}
user=${DB_USER:-${POSTGRES_USER:-postgres}}
database=${DB_NAME:-${POSTGRES_DB:-postgres}}
export PGPASSWORD=${DB_PASSWORD:-${POSTGRES_PASSWORD:-postgres}}

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
