# Let's Encrypt Setup

This document explains how to obtain trusted TLS certificates using Certbot.

## 1. Verify DNS

Ensure that the A record from [DNS_SETUP.md](DNS_SETUP.md) already points to your server. The HTTP challenge will be served from this host.

```bash
# should return the server IP
nslookup crm-synergy.ru
```

## 2. Install Certbot

Certbot can run in a Docker container. Pull the image first:

```bash
docker pull certbot/certbot
```

Create a directory for challenge files and certificates if it does not exist:

```bash
mkdir -p infra/nginx/www infra/nginx/certs
```

## 3. Obtain the certificate

Run Certbot with the `webroot` plugin. The `nginx` service must be running so that
`/.well-known/acme-challenge` is reachable on port 80.

```bash
docker run --rm \
  -v "$PWD/infra/nginx/www:/var/www/certbot" \
  -v "$PWD/infra/nginx/certs:/etc/letsencrypt" \
  certbot/certbot certonly \
    --webroot -w /var/www/certbot \
    -d crm-synergy.ru
```

After success copy the resulting files:

```bash
cp infra/nginx/certs/live/crm-synergy.ru/fullchain.pem infra/nginx/certs/server.crt
cp infra/nginx/certs/live/crm-synergy.ru/privkey.pem infra/nginx/certs/server.key
```

## 4. Automatic renewal

Create a systemd timer or cron job that runs the same Docker command with the
`renew` subcommand:

```bash
0 3 * * * docker run --rm \
  -v /path/to/repo/infra/nginx/www:/var/www/certbot \
  -v /path/to/repo/infra/nginx/certs:/etc/letsencrypt \
  certbot/certbot renew --quiet
```

After renewal restart the containers to pick up the new certificates:

```bash
docker compose -f infra/docker-compose.yml restart nginx
```

## 5. Restart services

When the certificate is obtained for the first time (or after renewal) reload
NGINX:

```bash
docker compose -f infra/docker-compose.yml exec nginx nginx -s reload
```

If NGINX is not running yet start the full stack:

```bash
make up
```
