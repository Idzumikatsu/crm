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
| `RABBITMQ_HOST` | `rabbitmq` | `infra/k8s/helm/schedule-app/templates/backend-deployment.yaml` | chart values |
| `RABBITMQ_PORT` | `5672` | `infra/k8s/helm/schedule-app/templates/backend-deployment.yaml` | chart values |
| `RABBITMQ_USER` | `user` | `infra/k8s/helm/schedule-app/templates/rabbitmq-statefulset.yaml` | chart values |
| `RABBITMQ_PASSWORD` | `secret` | `infra/k8s/helm/schedule-app/templates/rabbitmq-statefulset.yaml` | chart values |
| `SMTP_HOST` | `smtp.example.com` | `backend/src/main/resources/application.yml` | `infra/.env` or secrets |
| `SMTP_PORT` | `587` | `backend/src/main/resources/application.yml` | `infra/.env` or secrets |
| `SMTP_USERNAME` | `user@example.com` | `backend/src/main/resources/application.yml` | `infra/.env` or secrets |
| `SMTP_PASSWORD` | `secret` | `backend/src/main/resources/application.yml` | `infra/.env` or secrets |
| `SMTP_AUTH` | `true` | `backend/src/main/resources/application.yml` | `infra/.env` |
| `SMTP_STARTTLS` | `true` | `backend/src/main/resources/application.yml` | `infra/.env` |
| `MAIL_FROM` | `no-reply@example.com` | `backend/src/main/resources/application.yml` | `infra/.env` |
| `TELEGRAM_BOT_TOKEN` | `123456:ABC` | `backend/src/main/resources/application.yml` | `infra/.env` or secrets |
| `JWT_SECRET` | `0123456789abcdef0123456789abcdef` | `backend/src/main/resources/application.yml`, `infra/docker-compose.yml` | `infra/.env` or secrets |
| `ALLOWED_ORIGINS` | `http://localhost:5173` | `backend/src/main/java/com/example/scheduletracker/config/CorsConfig.java` | `infra/.env` |
| `APP_HOST` | `app` | `infra/nginx/nginx.conf.template`, `infra/docker-compose.yml`, `scripts/render-nginx.sh`, `infra/nginx/docker-entrypoint.sh` | `infra/.env` |
| `APP_PORT` | `8080` | `infra/nginx/nginx.conf.template`, `infra/docker-compose.yml`, `scripts/render-nginx.sh`, `infra/nginx/docker-entrypoint.sh` | `infra/.env` |
| `SERVER_NAME` | `example.com` | `infra/nginx/nginx.conf.template`, `infra/docker-compose.yml`, `scripts/render-nginx.sh`, `scripts/letsencrypt.sh` | `infra/.env` |
| `NGINX_HTTP_PORT` | `8080` | `infra/docker-compose.yml` | `infra/.env` |
| `NGINX_HTTPS_PORT` | `8443` | `infra/docker-compose.yml` | `infra/.env` |
| `NGINX_LOG_PATH` | `infra/nginx/logs` | docker-compose volume | n/a |
| `OTEL_EXPORTER_OTLP_ENDPOINT` | `http://otel-collector:4318` | `infra/app.yml` | `infra/.env` |
| `OTEL_EXPORTER_OTLP_PROTOCOL` | `http/protobuf` | `infra/app.yml` | `infra/.env` |

`NGINX_HTTP_PORT` and `NGINX_HTTPS_PORT` configure host ports for the reverse proxy. Change them if the
defaults (`8080` and `8443`) conflict with other services on the host. `NGINX_LOG_PATH` defines a directory on the host where access and error logs will be written so they persist across restarts.
| `SPRING_PROFILES_ACTIVE` | `postgres` | `infra/docker-compose.yml` | `infra/.env` |
| `PROXY_HOST` | `proxy.example.com` | `scripts/setup-proxy.sh`, `.github/workflows/deploy.yml` | secrets or shell |
| `PROXY_PORT` | `8080` | `scripts/setup-proxy.sh`, `.github/workflows/deploy.yml` | secrets or shell |
| `HTTP_PROXY` | `http://proxy.example.com:8080` | `scripts/setup-proxy.sh` | shell |
| `HTTPS_PROXY` | `http://proxy.example.com:8080` | `scripts/setup-proxy.sh` | shell |
| `BACKUP_DIR` | `backups` | `scripts/backup-db.sh`, `infra/k8s/helm/schedule-app/templates/backup-cronjob.yaml` | shell |
| `VITE_API_URL` |  | `frontend/src/api.ts` | `frontend/.env` |
| `DEPLOY_DIR` | `myapp` | `.github/workflows/deploy.yml` | workflow env |
| `VPS_HOST` | `203.0.113.1` | `.github/workflows/deploy.yml` | secrets |
| `VPS_USER` | `deploy` | `.github/workflows/deploy.yml` | secrets |
| `VPS_SSH_KEY` | contents of `id_rsa` | `.github/workflows/deploy.yml` | secrets |
| `VPS_PASSPHRASE` | `secret` | `.github/workflows/deploy.yml` | secrets |
| `VPS_SSH_PORT` | `22` | `.github/workflows/deploy.yml` | secrets |
| `DOCKER_REPOSITORY` | `example/schedule` | `.github/workflows/deploy-k8s.yml` | secrets |
| `DOCKER_USERNAME` | `dockeruser` | `.github/workflows/deploy-k8s.yml` | secrets |
| `DOCKER_PASSWORD` | `secret` | `.github/workflows/deploy-k8s.yml` | secrets |
| `KUBE_CONFIG_B64` | output of `base64 -w0 ~/.kube/config` | `.github/workflows/deploy-k8s.yml` | secrets |
| `CERTBOT_EMAIL` | `admin@example.com` | `scripts/letsencrypt.sh` | shell |
| `DOMAIN` | `example.com` | `scripts/letsencrypt.sh` | shell |
| `SSL_CERT` | contents of `crm-synergy.crt` (PEM or Base64) | `.github/workflows/deploy.yml` | secrets/vars |
| `SSL_CA_CERT` | contents of `crm-synergy_ca.crt` (PEM or Base64) | `.github/workflows/deploy.yml` | secrets/vars |
| `SSL_KEY`  | contents of `crm-synergy.key` (PEM or Base64) | `.github/workflows/deploy.yml` | secrets/vars |

In production the frontend should leave `VITE_API_URL` empty because the base API path is already included in all fetch calls.

