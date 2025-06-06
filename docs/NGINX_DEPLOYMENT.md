# NGINX Deployment Process

This document describes how NGINX configuration changes are delivered and how traffic is gradually shifted to new versions.

## Repository and Reviews
Configuration lives in the `nginx/` directory of the main repository. All changes go through pull requests and require at least one approval before merge.

## CI Validation
GitHub Actions renders `nginx.conf` from `nginx.conf.template` using environment variables and runs `nginx -t` inside the official container. The workflow fails if the syntax is invalid.

## Rollout Strategy
Deployments start with one instance receiving a portion of traffic while the previous version stays active. Metrics and error rates are monitored for 5 minutes. If no alerts trigger, all instances are updated. On failures the workflow automatically rolls back by redeploying the previous Docker image.

## Delivery Steps
1. Push to `main` triggers the `deploy.yml` workflow.
2. The application JAR and rendered `nginx.conf` are copied to the VPS.
3. `docker compose up -d` rebuilds containers.
4. After success, traffic is gradually switched according to the strategy above.

5. When all checks pass the pull request is auto-merged.
