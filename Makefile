.PHONY: frontend k8s-deploy k8s-delete k8s-render

REGISTRY ?= ghcr.io/idzumikatsu/trash

frontend:
	npm --prefix frontend run build

k8s-deploy:
        helm upgrade --install schedule-app infra/k8s/helm/schedule-app \
                -f infra/k8s/helm/schedule-app/values.yaml \
                --set image.repository=$(REGISTRY)/backend \
                --set frontend.image=$(REGISTRY)/frontend \
                --atomic --wait

k8s-delete:
	helm uninstall schedule-app || true

k8s-render:
	helm template schedule-app infra/k8s/helm/schedule-app \
		-f infra/k8s/helm/schedule-app/values.yaml > rendered.yaml

