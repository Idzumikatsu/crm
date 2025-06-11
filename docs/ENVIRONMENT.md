# Environment Variables

All configuration is provided via environment variables. Create `infra/.env` and
`frontend/.env` manually if you need to override the defaults—`.env.example` is
no longer part of the workflow. The table below lists each variable, an example
value, where it is used and the recommended storage location. When a variable is
not defined, Docker Compose falls back to sensible defaults suitable for local
development.

| Variable | Example | Consumed In | Location |
| --- | --- | --- | --- |
| `DB_HOST` | `db` | `backend/src/main/resources/application-postgres.yml`, `infra/docker-compose.yml`, `scripts/wait-for-db.sh`, `scripts/backup-db.sh` | `infra/.env` |
| `DB_PORT` | `5432` | `backend/src/main/resources/application-postgres.yml`, `infra/docker-compose.yml`, `scripts/wait-for-db.sh`, `scripts/backup-db.sh` | `infra/.env` |
| `DB_USER` | `postgres` | `backend/src/main/resources/application-postgres.yml`, `infra/docker-compose.yml`, `scripts/wait-for-db.sh`, `scripts/backup-db.sh` | `infra/.env` |
| `DB_PASSWORD` | `postgres` | `backend/src/main/resources/application-postgres.yml`, `infra/docker-compose.yml`, `scripts/wait-for-db.sh`, `scripts/backup-db.sh` | `infra/.env` |
| `DB_NAME` | `schedule` | `backend/src/main/resources/application-postgres.yml`, `infra/docker-compose.yml`, `scripts/wait-for-db.sh`, `scripts/backup-db.sh` | `infra/.env` |
| `POSTGRES_DB` | `schedule` | `infra/docker-compose.yml` | `infra/.env` |
| `POSTGRES_USER` | `postgres` | `infra/docker-compose.yml` | `infra/.env` |
| `POSTGRES_PASSWORD` | `postgres` | `infra/docker-compose.yml` | `infra/.env` |
| `SMTP_HOST` | `smtp.example.com` | `backend/src/main/resources/application.yml` | `infra/.env` or secrets |
| `SMTP_PORT` | `587` | `backend/src/main/resources/application.yml` | `infra/.env` or secrets |
| `SMTP_USERNAME` | `user@example.com` | `backend/src/main/resources/application.yml` | `infra/.env` or secrets |
| `SMTP_PASSWORD` | `secret` | `backend/src/main/resources/application.yml` | `infra/.env` or secrets |
| `SMTP_AUTH` | `true` | `backend/src/main/resources/application.yml` | `infra/.env` |
| `SMTP_STARTTLS` | `true` | `backend/src/main/resources/application.yml` | `infra/.env` |
| `MAIL_FROM` | `no-reply@example.com` | `backend/src/main/resources/application.yml` | `infra/.env` |
| `TELEGRAM_BOT_TOKEN` | `123456:ABC` | `backend/src/main/resources/application.yml` | `infra/.env` or secrets |
| `JWT_SECRET` | `0123456789abcdef0123456789abcdef` | `backend/src/main/resources/application.yml`, `infra/docker-compose.yml` | `infra/.env` or secrets |
| `APP_HOST` | `app` | `infra/nginx/nginx.conf.template`, `infra/docker-compose.yml`, `scripts/render-nginx.sh` | `infra/.env` |
| `APP_PORT` | `8080` | `infra/nginx/nginx.conf.template`, `infra/docker-compose.yml`, `scripts/render-nginx.sh` | `infra/.env` |
| `SERVER_NAME` | `example.com` | `infra/nginx/nginx.conf.template`, `infra/docker-compose.yml`, `scripts/render-nginx.sh`, `scripts/letsencrypt.sh` | `infra/.env` |
| `NGINX_HTTP_PORT` | `80` | `infra/docker-compose.yml` | `infra/.env` |
| `NGINX_HTTPS_PORT` | `443` | `infra/docker-compose.yml` | `infra/.env` |

Both variables configure host ports for the reverse proxy. Change them if the
defaults (`80` and `443`) conflict with other services on the host.
| `SPRING_PROFILES_ACTIVE` | `postgres` | `infra/docker-compose.yml` | `infra/.env` |
| `PROXY_HOST` | `proxy.example.com` | `scripts/setup-proxy.sh`, `.github/workflows/deploy.yml` | secrets or shell |
| `PROXY_PORT` | `8080` | `scripts/setup-proxy.sh`, `.github/workflows/deploy.yml` | secrets or shell |
| `HTTP_PROXY` | `http://proxy.example.com:8080` | `scripts/setup-proxy.sh` | shell |
| `HTTPS_PROXY` | `http://proxy.example.com:8080` | `scripts/setup-proxy.sh` | shell |
| `BACKUP_DIR` | `backups` | `scripts/backup-db.sh` | shell |
| `VITE_API_URL` | `https://example.com` | `frontend/src/api.ts` | `frontend/.env` |
| `DEPLOY_DIR` | `myapp` | `.github/workflows/deploy.yml` | workflow env |
| `VPS_HOST` | `203.0.113.1` | `.github/workflows/deploy.yml` | secrets |
| `VPS_USER` | `deploy` | `.github/workflows/deploy.yml` | secrets |
| `VPS_PASSWORD` | `password` | `.github/workflows/deploy.yml` | secrets |
| `VPS_PORT` | `22` | `.github/workflows/deploy.yml` | secrets |
| `CERTBOT_EMAIL` | `admin@example.com` | `scripts/letsencrypt.sh` | shell |
| `DOMAIN` | `example.com` | `scripts/letsencrypt.sh` | shell |
| `SSL_CERT` | contents of `crm-synergy.crt` (PEM or Base64) | `.github/workflows/deploy.yml` | secrets/vars |
| `SSL_CA_CERT` | contents of `crm-synergy_ca.crt` (PEM or Base64) | `.github/workflows/deploy.yml` | secrets/vars |
| `SSL_KEY`  | contents of `crm-synergy.key` (PEM or Base64) | `.github/workflows/deploy.yml` | secrets/vars |

