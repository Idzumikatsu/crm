# SMTP Configuration

The backend sends verification emails using the following environment variables:

| Variable | Default | Description |
| --- | --- | --- |
| `SMTP_HOST` | `localhost` | SMTP server hostname |
| `SMTP_PORT` | `25` | SMTP server port |
| `SMTP_USERNAME` | (empty) | SMTP account username |
| `SMTP_PASSWORD` | (empty) | SMTP account password |
| `SMTP_AUTH` | `false` | Enable SMTP AUTH |
| `SMTP_STARTTLS` | `false` | Enable STARTTLS |
| `MAIL_FROM` | `no-reply@example.com` | Sender address for outgoing mail |

Place these variables in `infra/.env` when running Docker Compose or export them for local development.
