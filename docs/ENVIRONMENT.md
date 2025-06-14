# Environment Variables

All configuration is provided via environment variables. `.env` files such as
`infra/.env` and `frontend/.env` should exist only on the server when you need
to override the defaults—`.env.example` is no longer part of the workflow. The
table below lists each variable, an example value, where it is used and the
recommended storage location. When a variable is not defined, production
defaults are applied.

| Variable | Example | Consumed In | Location |
| --- | --- | --- | --- |
| `DB_HOST` | `db` | `backend/src/main/resources/application-postgres.yml`, `scripts/backup-db.sh` | `infra/.env` |
| `DB_PORT` | `5432` | `backend/src/main/resources/application-postgres.yml`, `scripts/backup-db.sh` | `infra/.env` |
| `DB_USER` | `postgres` | `backend/src/main/resources/application-postgres.yml`, `scripts/backup-db.sh` | `infra/.env` |
| `DB_PASSWORD` | `postgres` | `backend/src/main/resources/application-postgres.yml`, `scripts/backup-db.sh` | `infra/.env` |
| `DB_NAME` | `schedule` | `backend/src/main/resources/application-postgres.yml`, `scripts/backup-db.sh` | `infra/.env` |
| `SMTP_HOST` | `smtp.example.com` | `backend/src/main/resources/application.yml` | `infra/.env` or secrets |
| `SMTP_PORT` | `587` | `backend/src/main/resources/application.yml` | `infra/.env` or secrets |
| `SMTP_USERNAME` | `user@example.com` | `backend/src/main/resources/application.yml` | `infra/.env` or secrets |
| `SMTP_PASSWORD` | `secret` | `backend/src/main/resources/application.yml` | `infra/.env` or secrets |
| `SMTP_AUTH` | `true` | `backend/src/main/resources/application.yml` | `infra/.env` |
| `SMTP_STARTTLS` | `true` | `backend/src/main/resources/application.yml` | `infra/.env` |
| `MAIL_FROM` | `no-reply@example.com` | `backend/src/main/resources/application.yml` | `infra/.env` |
| `TELEGRAM_BOT_TOKEN` | `123456:ABC` | `backend/src/main/resources/application.yml` | `infra/.env` or secrets |
| `JWT_SECRET` | `0123456789abcdef0123456789abcdef` | `backend/src/main/resources/application.yml` | `infra/.env` or secrets |
| `ALLOWED_ORIGINS` | `https://app.example.com` | `backend/src/main/java/com/example/scheduletracker/config/CorsConfig.java` | `infra/.env` |

| `SPRING_PROFILES_ACTIVE` | `postgres` |  | `infra/.env` |
| `VITE_API_URL` |  | `frontend/src/api.ts` | `frontend/.env` |

In production the frontend should leave `VITE_API_URL` empty because the base API path is already included in all fetch calls.

