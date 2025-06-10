#!/usr/bin/env bash
set -euo pipefail

repo_root="$(cd "$(dirname "$0")/.." && pwd)"
webroot="$repo_root/infra/nginx/www"
certdir="$repo_root/infra/nginx/certs"

mkdir -p "$webroot" "$certdir"

domain="${DOMAIN:-${SERVER_NAME:-crm-synergy.ru}}"
email="${CERTBOT_EMAIL:-izumi.katsu667@gmail.com}"

if [ ! -f "$certdir/crm-synergy.crt" ] || [ ! -f "$certdir/crm-synergy.key" ]; then
  openssl req -x509 -newkey rsa:2048 -nodes \
    -keyout "$certdir/crm-synergy.key" \
    -out "$certdir/crm-synergy.crt" \
    -days 1 -subj "/CN=$domain"
fi

# ensure nginx is running to serve challenges
if ! docker compose -f "$repo_root/infra/docker-compose.yml" ps nginx >/dev/null 2>&1; then
  docker compose -f "$repo_root/infra/docker-compose.yml" up -d nginx
fi

docker run --rm \
  -v "$webroot:/var/www/certbot" \
  -v "$certdir:/etc/letsencrypt" \
  certbot/certbot certonly \
    --webroot -w /var/www/certbot \
    --non-interactive \
    --agree-tos \
    -m "$email" \
    -d "$domain"

cp "$certdir/live/$domain/fullchain.pem" "$certdir/crm-synergy.crt"
cp "$certdir/live/$domain/privkey.pem" "$certdir/crm-synergy.key"

docker compose -f "$repo_root/infra/docker-compose.yml" exec nginx nginx -s reload
