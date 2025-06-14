# NGINX Deployment Process

The project uses a standalone NGINX instance as a reverse proxy in front of the Spring Boot backend.

Configuration files live on the server and are deployed together with the systemd unit. Cluster manifests are no longer maintained.

