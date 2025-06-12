# NGINX Design

## Target Architecture

Initially the solution ran inside Docker Compose. As part of the
Kubernetes migration traffic now enters the cluster through the NGINX
Ingress Controller which proxies requests to the Spring Boot backend. The
application communicates with PostgreSQL while NGINX exposes metrics via a
Prometheus exporter sidecar.

```
Client ---> NGINX ---> App ---> PostgreSQL
                  |
                  --> Prometheus Exporter
```

NGINX is deployed as a standalone service in front of the application.
Load balancing between multiple application instances will use the default
`round_robin` strategy. Clients today support HTTP/1.1 and HTTP/2, so we
plan to enable HTTP/2 on the public interface. HTTP/3 is not required yet.

Releases of NGINX configuration follow a blue‑green strategy to minimise
risk. The approved architecture and these decisions are now recorded in
this design document.
