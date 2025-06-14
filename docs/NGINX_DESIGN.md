# NGINX Design

## Target Architecture

Traffic reaches the application through an NGINX reverse proxy.
The Spring Boot backend communicates with PostgreSQL while NGINX exposes metrics via a Prometheus exporter sidecar.

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
