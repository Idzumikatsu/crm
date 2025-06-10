#!/usr/bin/env bash
# Dump PostgreSQL database with timestamp and gzip
set -euo pipefail

: "${DB_HOST:?DB_HOST is required}"
: "${DB_PORT:?DB_PORT is required}"
: "${DB_USER:?DB_USER is required}"
: "${DB_PASSWORD:?DB_PASSWORD is required}"
: "${DB_NAME:?DB_NAME is required}"

host=${DB_HOST}
port=${DB_PORT}
user=${DB_USER}
db=${DB_NAME}
export PGPASSWORD=${DB_PASSWORD}

backup_dir=${BACKUP_DIR:-backups}
mkdir -p "$backup_dir"

stamp=$(date +%Y%m%d_%H%M%S)
file="$backup_dir/${db}_$stamp.sql"

pg_dump -h "$host" -p "$port" -U "$user" "$db" > "$file"
gzip "$file"

echo "Backup created: ${file}.gz"
