# NGINX Deployment Process

This document describes how NGINX configuration changes are delivered and how traffic is gradually shifted to new versions.

## Repository and Reviews
Configuration lives in the `nginx/` directory of the main repository. All changes go through pull requests and require at least one approval before merge.

## CI Validation
GitHub Actions renders `nginx.conf` from `nginx.conf.template` using environment variables and runs `nginx -t` inside the official container. The workflow fails if the syntax is invalid.
The `nginx-smoke-test.yml` workflow additionally builds the image with `docker buildx build --load`, starts it on a temporary port, and performs a simple `curl` request to catch runtime issues. Build layers are cached using the GitHub Actions cache for faster rebuilds.

At runtime the container executes `/docker-entrypoint.sh`. This script substitutes `$APP_HOST`, `$APP_PORT` and `$SERVER_NAME` into `nginx.conf.template` using `envsubst` and then launches NGINX in the foreground. This allows the same image to be reused across environments.

## Rollout Strategy
Deployments start with one instance receiving a portion of traffic while the previous version stays active. Metrics and error rates are monitored for 5 minutes. If no alerts trigger, all instances are updated. On failures the workflow automatically rolls back by redeploying the previous Docker image.

## Delivery Steps
1. Push to `main` triggers the `deploy.yml` workflow.
2. Frontend sources are linted with `eslint --fix` and checked for errors.
3. The backend is built from the `backend/` directory using `./gradlew build`.
4. The application JAR and rendered `nginx.conf` are copied to the VPS.
5. `helm upgrade --install schedule-app infra/k8s/helm/schedule-app --atomic --wait` deploys the new configuration.
6. After success, traffic is gradually switched according to the strategy above.

7. When all checks pass the pull request is auto-merged.
