#!/usr/bin/env bash
set -e
host=${DB_HOST:-localhost}
port=${DB_PORT:-5432}
for i in {1..60}; do
  if pg_isready -h "$host" -p "$port" >/dev/null 2>&1; then
    exec "$@"
  fi
  sleep 1
  echo "Waiting for $host:$port..."
done
echo "Timeout waiting for $host:$port" >&2
exit 1
