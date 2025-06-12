.PHONY: frontend k8s-deploy k8s-delete k8s-render

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

