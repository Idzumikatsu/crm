# NGINX Deployment Process

The project previously included a standalone NGINX container. After migrating to Kubernetes all traffic is terminated by the NGINX Ingress Controller. Therefore no NGINX configuration files are stored in the repository.

Ingress resources are defined in the Helm chart under `infra/k8s/helm/schedule-app`. Updating the chart and running `helm upgrade --install` deploys the new rules together with the application.

