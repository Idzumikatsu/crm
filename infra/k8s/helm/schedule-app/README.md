# schedule-app Helm Chart

This chart deploys the monolithic Schedule Tracker application together with a PostgreSQL database and RabbitMQ instance.
It targets Kubernetes clusters running version 1.30 or newer and follows the design outlined in
[../../../../docs/KUBERNETES_DESIGN.md](../../../../docs/KUBERNETES_DESIGN.md).

The chart defines:

- `Deployment` and `Service` for the Spring Boot backend
- `StatefulSet` and `Service` for PostgreSQL
- `StatefulSet` and `Service` for RabbitMQ
- `Ingress` resource configured for the NGINX Ingress Controller
  - `HorizontalPodAutoscaler` for scaling the backend

Values controlling credentials:

- `postgresql.username` / `postgresql.password`
- `rabbitmq.username` / `rabbitmq.password`
- `jwtSecret` used by the backend
- `resources` for the backend container requests and limits

Default values assume a demo environment. Sensitive strings like database and
RabbitMQ passwords are stored in a `Secret` generated from `values.yaml`.
Adjust the container images and credentials before deploying to production:

```bash
helm upgrade --install schedule-app infra/k8s/helm/schedule-app \
  --values infra/k8s/helm/schedule-app/values.yaml
```

