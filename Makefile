COMPOSE_FILE ?= infra/docker-compose.yml

.PHONY: build up down logs frontend k8s-deploy k8s-delete k8s-render

build:
	docker compose -f $(COMPOSE_FILE) build

up:
	docker compose -f $(COMPOSE_FILE) up -d

down:
	docker compose -f infra/docker-compose.yml down --remove-orphans

logs:
	docker compose -f $(COMPOSE_FILE) logs -f

frontend:
	npm --prefix frontend run build

k8s-deploy:
	helm upgrade --install schedule-app infra/k8s/helm/schedule-app \
		-f infra/k8s/helm/schedule-app/values.yaml \
		--atomic --wait

k8s-delete:
	helm uninstall schedule-app || true

k8s-render:
	helm template schedule-app infra/k8s/helm/schedule-app \
		-f infra/k8s/helm/schedule-app/values.yaml > rendered.yaml

