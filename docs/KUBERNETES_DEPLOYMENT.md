# Kubernetes Deployment

This document describes the CI workflow that deploys Schedule Tracker to a Kubernetes cluster using Helm.

The pipeline builds Docker images, pushes them to a registry and runs `helm upgrade --install`.

## CI workflow

1. The workflow is defined in `.github/workflows/deploy-k8s.yml`.
2. On each push to `main` it builds the backend image using the provided Dockerfile.
3. The image is tagged with the commit SHA and uploaded to the registry defined by `DOCKER_REPOSITORY`.
4. Helm is configured using the kubeconfig stored in `KUBE_CONFIG_B64`.
5. `helm upgrade --install` renders the chart from `infra/k8s/helm/schedule-app` with the new image tag.

## Required secrets

- `DOCKER_REPOSITORY`, `DOCKER_USERNAME`, `DOCKER_PASSWORD` – credentials for pushing images.
- `KUBE_CONFIG_B64` – base64-encoded kubeconfig granting access to the cluster.

Adjust values in `infra/k8s/helm/schedule-app/values.yaml` for production before running the workflow.
Set appropriate CPU and memory limits under the `resources` key so pods are scheduled with reserved capacity.
