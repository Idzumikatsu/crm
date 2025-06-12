# schedule-app Helm Chart

This chart deploys the monolithic Schedule Tracker application together with a PostgreSQL database and RabbitMQ instance.
It targets Kubernetes clusters running version 1.30 or newer and follows the design outlined in
[../../../../docs/KUBERNETES_DESIGN.md](../../../../docs/KUBERNETES_DESIGN.md).

The chart defines:

- `Deployment` and `Service` for the Spring Boot backend
- `Deployment` and `Service` for the React frontend
- `StatefulSet` and `Service` for PostgreSQL
- `StatefulSet` and `Service` for RabbitMQ
- `Ingress` resource configured for the NGINX Ingress Controller
  - `HorizontalPodAutoscaler` for scaling the backend
- `CronJob` for periodic database backups

Values controlling credentials and connectivity:

- `postgresql.enabled` – set to `false` to use an external database
- `postgresql.host` / `postgresql.port`
- `postgresql.username` / `postgresql.password`
- `rabbitmq.enabled` – set to `false` to use an external broker
- `rabbitmq.host` / `rabbitmq.port`
- `rabbitmq.username` / `rabbitmq.password`
- `replicaCount` to set the default number of backend pods
- `frontend.*` to configure the container image and resources for the React SPA
- `autoscaling.*` to tune or disable the Horizontal Pod Autoscaler
- `serviceMonitor.enabled` to expose Prometheus metrics
- `pdb.*` for the PodDisruptionBudget
- `serviceAccount.*` to create or reuse a Kubernetes ServiceAccount
- `jwtSecret` used by the backend
- `resources` for the backend container requests and limits
- `migrations.enabled` to run Flyway migrations via a pre-install and pre-upgrade Job
- `backup.enabled` to schedule periodic `pg_dump` backups via a CronJob
- `backup.schedule` CRON expression defining when the backup runs
- `backup.persistence.size` size of the PVC storing dumps

When `migrations.enabled` is `true`, a short-lived Job waits for the database using `wait-for-db.sh`, applies migrations and is removed automatically after completion.

Default values assume a demo environment. Sensitive strings like database and
RabbitMQ passwords are stored in a `Secret` generated from `values.yaml`.
Adjust the container images and credentials before deploying to production. A
sample configuration is provided in `values-production.yaml`:

```bash
helm upgrade --install schedule-app infra/k8s/helm/schedule-app \
  --values infra/k8s/helm/schedule-app/values-production.yaml \
  --set image.repository=<registry>/schedule-backend \
  --set frontend.image=<registry>/schedule-frontend
```

