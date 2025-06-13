# Environment Variables

All configuration is provided via environment variables. Create `infra/.env` and
`frontend/.env` manually if you need to override the defaults—`.env.example` is
no longer part of the workflow. The table below lists each variable, an example
value, where it is used and the recommended storage location. When a variable is
not defined, defaults are applied for local
development.

| Variable | Example | Consumed In | Location |
| --- | --- | --- | --- |
| `DB_HOST` | `db` | `backend/src/main/resources/application-postgres.yml`, `scripts/wait-for-db.sh`, `scripts/backup-db.sh` | `infra/.env` |
| `DB_PORT` | `5432` | `backend/src/main/resources/application-postgres.yml`, `scripts/wait-for-db.sh`, `scripts/backup-db.sh` | `infra/.env` |
| `DB_USER` | `postgres` | `backend/src/main/resources/application-postgres.yml`, `scripts/wait-for-db.sh`, `scripts/backup-db.sh` | `infra/.env` |
| `DB_PASSWORD` | `postgres` | `backend/src/main/resources/application-postgres.yml`, `scripts/wait-for-db.sh`, `scripts/backup-db.sh` | `infra/.env` |
| `DB_NAME` | `schedule` | `backend/src/main/resources/application-postgres.yml`, `scripts/wait-for-db.sh`, `scripts/backup-db.sh` | `infra/.env` |
| `POSTGRES_DB` | `schedule` |  | `infra/.env` |
| `POSTGRES_USER` | `postgres` |  | `infra/.env` |
| `POSTGRES_PASSWORD` | `postgres` |  | `infra/.env` |
| `RABBITMQ_HOST` | `rabbitmq` | `infra/k8s/helm/schedule-app/templates/backend-deployment.yaml` | chart values, `infra/.env` |
| `RABBITMQ_PORT` | `5672` | `infra/k8s/helm/schedule-app/templates/backend-deployment.yaml` | chart values, `infra/.env` |
| `RABBITMQ_USER` | `user` | `infra/k8s/helm/schedule-app/templates/rabbitmq-statefulset.yaml` | chart values, `infra/.env` |
| `RABBITMQ_PASSWORD` | `secret` | `infra/k8s/helm/schedule-app/templates/rabbitmq-statefulset.yaml` | chart values, `infra/.env` |
| `SMTP_HOST` | `smtp.example.com` | `backend/src/main/resources/application.yml` | `infra/.env` or secrets |
| `SMTP_PORT` | `587` | `backend/src/main/resources/application.yml` | `infra/.env` or secrets |
| `SMTP_USERNAME` | `user@example.com` | `backend/src/main/resources/application.yml` | `infra/.env` or secrets |
| `SMTP_PASSWORD` | `secret` | `backend/src/main/resources/application.yml` | `infra/.env` or secrets |
| `SMTP_AUTH` | `true` | `backend/src/main/resources/application.yml` | `infra/.env` |
| `SMTP_STARTTLS` | `true` | `backend/src/main/resources/application.yml` | `infra/.env` |
| `MAIL_FROM` | `no-reply@example.com` | `backend/src/main/resources/application.yml` | `infra/.env` |
| `TELEGRAM_BOT_TOKEN` | `123456:ABC` | `backend/src/main/resources/application.yml` | `infra/.env` or secrets |
| `JWT_SECRET` | `0123456789abcdef0123456789abcdef` | `backend/src/main/resources/application.yml` | `infra/.env` or secrets |
| `ALLOWED_ORIGINS` | `http://localhost:5173` | `backend/src/main/java/com/example/scheduletracker/config/CorsConfig.java` | `infra/.env` |
| `APP_HOST` | `app` | `infra/nginx/nginx.conf.template`, `infra/nginx/docker-entrypoint.sh` | `infra/.env` |
| `APP_PORT` | `8080` | `infra/nginx/nginx.conf.template`, `infra/nginx/docker-entrypoint.sh` | `infra/.env` |
| `SERVER_NAME` | `example.com` | `infra/nginx/nginx.conf.template` | `infra/.env` |
| `NGINX_HTTP_PORT` | `8080` |  | `infra/.env` |
| `NGINX_HTTPS_PORT` | `8443` |  | `infra/.env` |
| `NGINX_LOG_PATH` | `infra/nginx/logs` | host path | n/a |
| `OTEL_EXPORTER_OTLP_ENDPOINT` | `http://otel-collector:4318` | `infra/app.yml` | `infra/.env` |
| `OTEL_EXPORTER_OTLP_PROTOCOL` | `http/protobuf` | `infra/app.yml` | `infra/.env` |

`NGINX_HTTP_PORT` and `NGINX_HTTPS_PORT` configure host ports for the reverse proxy. Change them if the
defaults (`8080` and `8443`) conflict with other services on the host. `NGINX_LOG_PATH` defines a directory on the host where access and error logs will be written so they persist across restarts.
| `SPRING_PROFILES_ACTIVE` | `postgres` |  | `infra/.env` |
| `PROXY_HOST` | `proxy.example.com` | `scripts/setup-proxy.sh` | secrets or shell |
| `PROXY_PORT` | `8080` | `scripts/setup-proxy.sh` | secrets or shell |
| `HTTP_PROXY` | `http://proxy.example.com:8080` | `scripts/setup-proxy.sh` | shell |
| `HTTPS_PROXY` | `http://proxy.example.com:8080` | `scripts/setup-proxy.sh` | shell |
| `BACKUP_DIR` | `backups` | `scripts/backup-db.sh`, `infra/k8s/helm/schedule-app/templates/backup-cronjob.yaml` | shell |
| `VITE_API_URL` |  | `frontend/src/api.ts` | `frontend/.env` |
| `KUBE_CONFIG_B64` | output of `base64 -w0 ~/.kube/config` | `.github/workflows/deploy-k8s.yml` | secrets |

In production the frontend should leave `VITE_API_URL` empty because the base API path is already included in all fetch calls.

