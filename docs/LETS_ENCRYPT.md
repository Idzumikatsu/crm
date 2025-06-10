# Let's Encrypt Setup

This document explains how trusted TLS certificates are obtained using Certbot.
The GitHub Actions deployment workflow automatically executes
`scripts/letsencrypt.sh` on the server. The script requests a certificate via the
webroot plugin and reloads NGINX when finished. Manual steps are kept below for
reference.

## 1. Verify DNS

Ensure that the A record from [DNS_SETUP.md](DNS_SETUP.md) already points to your server. The HTTP challenge will be served from this host.

```bash
# should return the server IP
nslookup english.webhop.me
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

If needed you can invoke the helper script manually. It ensures NGINX is
running, requests a certificate and copies it into `infra/nginx/certs`:

```bash
scripts/letsencrypt.sh
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
