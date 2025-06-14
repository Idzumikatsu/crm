.PHONY: frontend

REGISTRY ?= ghcr.io/idzumikatsu/trash

frontend:
	npm --prefix frontend run build


