COMPOSE_FILE ?= infra/docker-compose.yml

.PHONY: build up down logs frontend

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
