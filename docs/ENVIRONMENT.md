# Environment Variables

The backend relies on the following variables. They **must** be provided when running the application or Docker container.

| Variable | Description |
| --- | --- |
| `DB_HOST` | Database hostname |
| `DB_PORT` | Database port |
| `DB_USER` | Database username |
| `DB_PASSWORD` | Database password |
| `DB_NAME` | Database name |
| `POSTGRES_DB` | PostgreSQL database name |
| `POSTGRES_USER` | PostgreSQL user |
| `POSTGRES_PASSWORD` | PostgreSQL password |
| `SMTP_HOST` | SMTP server hostname |
| `SMTP_PORT` | SMTP server port |
| `SMTP_USERNAME` | SMTP account username |
| `SMTP_PASSWORD` | SMTP account password |
| `SMTP_AUTH` | Enable SMTP AUTH (`true` or `false`) |
| `SMTP_STARTTLS` | Enable STARTTLS (`true` or `false`) |
| `MAIL_FROM` | Sender address for outgoing mail |
| `TELEGRAM_BOT_TOKEN` | Telegram bot token |
| `JWT_SECRET` | Secret key used to sign JWT tokens |
| `APP_HOST` | Hostname of the backend container for Nginx |
| `APP_PORT` | Port of the backend container |
| `SERVER_NAME` | Domain served by Nginx |

Place these variables in `infra/.env` for Docker Compose or export them in the shell before running the application locally.
