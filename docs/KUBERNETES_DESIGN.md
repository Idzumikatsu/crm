# Kubernetes Design

This document defines the target architecture and technology stack for running
Schedule Tracker under Kubernetes. The goal is to migrate from Docker Compose
while keeping the deployment maintainable for the next 12–18 months.

## Goals

- Build container images for all components.
- Deploy the application to a Kubernetes cluster using Helm.
- Provide observability and basic automation from day one.

## Target stack

- **Kubernetes 1.30+** with containerd runtime.
- **Helm** for packaging manifests; charts live in `infra/k8s/helm`.
- **NGINX Ingress Controller** for external access.
- **PostgreSQL** as a StatefulSet or managed DB service.
- **RabbitMQ** for background tasks and notifications.
- **Prometheus** and **Grafana** for metrics.
- **Loki** for log aggregation.
- **Jaeger** and **OpenTelemetry** for tracing.
- **GitHub Actions** to build OCI images and run `helm upgrade --install`.
- Containers run as non-root wherever possible to satisfy Pod Security policies.
- Stateful services such as PostgreSQL and RabbitMQ request dedicated CPU and
  memory resources to ensure reliable performance.

## Services

- `backend` &ndash; Spring Boot API packaged as a Docker image.
- `frontend` &ndash; React SPA served via NGINX or the ingress controller.
- `rabbitmq` &ndash; message broker for asynchronous workflows.
- `postgresql` &ndash; primary data store.

## Deployment model

1. CI builds images using the existing Dockerfiles.
2. Charts render environment-specific values (staging, prod).
3. `helm upgrade --install` deploys to the cluster.
4. Horizontal Pod Autoscaler scales the backend based on CPU usage.

Configuration is passed through environment variables in `values.yaml` and
mounted Secrets. Persistent data for Postgres and RabbitMQ resides on
ProvisionedVolumes.

An initial Helm chart named `schedule-app` lives in
`infra/k8s/helm/schedule-app` and deploys the monolith together with a
PostgreSQL database and RabbitMQ broker. Database migrations are executed by a
`pre-install`/`pre-upgrade` Helm hook that waits for the database using the
`wait-for-db.sh` script. Completed migration Jobs are deleted automatically so
they do not clutter the namespace.

To protect data, a CronJob periodically executes `/backup-db.sh`. It dumps the
database to a PersistentVolume mounted at `/backups`. The job is disabled by
default and can be enabled via chart values.

## Observability

Prometheus scrapes metrics from the application and infrastructure. Grafana
presents dashboards. OpenTelemetry agents send traces to Jaeger. Logs are
collected via Fluent Bit and stored in Loki for easy querying.

## Mid‑term outlook

The first milestone is running the monolith on Kubernetes with automated
builds and releases. Once stable we will evaluate extracting separate services
and adopting a service mesh for traffic management. The stack above remains the
baseline for at least the next year.
