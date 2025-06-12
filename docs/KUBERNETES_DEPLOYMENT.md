# Kubernetes Deployment

This document describes the CI workflow that deploys Schedule Tracker to a Kubernetes cluster using Helm.

The pipeline builds Docker images, pushes them to a registry and runs `helm upgrade --install`.

## CI workflow

1. The workflow is defined in `.github/workflows/deploy-k8s.yml`.
2. On each push to `main` it builds the backend image using the provided Dockerfile.
3. The image is tagged with the commit SHA and uploaded to the registry defined by `DOCKER_REPOSITORY`.
4. Helm is configured using the kubeconfig stored in `KUBE_CONFIG_B64`.
5. `helm lint` verifies the chart before deployment to catch invalid manifests.
6. `helm upgrade --install` renders the chart from `infra/k8s/helm/schedule-app` with the new image tag. The command runs with `--atomic` and waits up to five minutes for rollout completion.

## Required secrets

- `DOCKER_REPOSITORY`, `DOCKER_USERNAME`, `DOCKER_PASSWORD` – credentials for pushing images.
- `KUBE_CONFIG_B64` – base64-encoded kubeconfig granting access to the cluster.

Adjust values in `infra/k8s/helm/schedule-app/values.yaml` for production before running the workflow.
Set appropriate CPU and memory limits under the `resources` key so pods are scheduled with reserved capacity.
A `Job` named `schedule-app-migrate` runs as a Helm hook before each installation or upgrade to apply database migrations. The job waits for the database and is cleaned up automatically after success.
All pods use a dedicated service account created by the chart, unless a custom
name is provided via `serviceAccount.name`.
When `backup.enabled` is true the chart also provisions a `CronJob` that dumps the database every night using `pg_dump`.
