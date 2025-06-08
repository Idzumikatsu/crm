#!/usr/bin/env bash
# Dump PostgreSQL database with timestamp and gzip
set -e

host=${DB_HOST:-db}
port=${DB_PORT:-5432}
user=${DB_USER:-postgres}
db=${DB_NAME:-schedule}
export PGPASSWORD=${DB_PASSWORD:-postgres}

backup_dir=${BACKUP_DIR:-backups}
mkdir -p "$backup_dir"

stamp=$(date +%Y%m%d_%H%M%S)
file="$backup_dir/${db}_$stamp.sql"

pg_dump -h "$host" -p "$port" -U "$user" "$db" > "$file"
gzip "$file"

echo "Backup created: ${file}.gz"
