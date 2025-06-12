DOCKER_COMPOSE ?= docker compose -f infra/compose.yml

.PHONY: build up down logs frontend k8s-deploy k8s-delete k8s-render

build:
        $(DOCKER_COMPOSE) build

up:
        $(DOCKER_COMPOSE) up -d

down:
        $(DOCKER_COMPOSE) down --remove-orphans

logs:
        $(DOCKER_COMPOSE) logs -f

frontend:
	npm --prefix frontend run build

k8s-deploy:
        helm upgrade --install schedule-app infra/k8s/helm/schedule-app \
                -f infra/k8s/helm/schedule-app/values.yaml \
                --set image.repository=${DOCKER_REPOSITORY}/schedule-backend \
                --set frontend.image=${DOCKER_REPOSITORY}/schedule-frontend \
                --atomic --wait

k8s-delete:
	helm uninstall schedule-app || true

k8s-render:
	helm template schedule-app infra/k8s/helm/schedule-app \
		-f infra/k8s/helm/schedule-app/values.yaml > rendered.yaml

