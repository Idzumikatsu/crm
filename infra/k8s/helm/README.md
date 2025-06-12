# Helm charts

This directory contains Helm charts for deploying the project to Kubernetes. The
`deploy.yml` workflow in GitHub Actions renders environment specific values and
runs `helm upgrade --install` against the target cluster.

Available charts:

- `schedule-app` – monolithic application with PostgreSQL, RabbitMQ and an ingress rule. Database migrations run as a Helm hook before each upgrade. The migration job waits for the database and is deleted after completion.

