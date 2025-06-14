# NGINX Deployment Process

The project uses a standalone NGINX instance as a reverse proxy in front of the Spring Boot backend.

Configuration files live on the server and are deployed together with the systemd unit. Cluster manifests are no longer maintained.

All requests are proxied to the Spring Boot application running on port `8080`. The SPA static files are packaged inside the JAR and served by the backend itself.

A sample configuration is provided in `infra/nginx/crm-synergy.conf`:

```nginx
server {
    listen 80;
    listen [::]:80;
    server_name crm-synergy.ru www.crm-synergy.ru;

    location /api/ {
        proxy_pass         http://127.0.0.1:8080;
        proxy_http_version 1.1;
        proxy_set_header   Host              $host;
        proxy_set_header   X-Real-IP         $remote_addr;
        proxy_set_header   X-Forwarded-For   $proxy_add_x_forwarded_for;
        proxy_set_header   X-Forwarded-Proto $scheme;
    }

    location / {
        proxy_pass         http://127.0.0.1:8080;
        proxy_http_version 1.1;
        proxy_set_header   Host              $host;
        proxy_set_header   X-Real-IP         $remote_addr;
        proxy_set_header   X-Forwarded-For   $proxy_add_x_forwarded_for;
        proxy_set_header   X-Forwarded-Proto $scheme;
    }
}
```

Deploy the file to `/etc/nginx/sites-available/` and create a symlink in `sites-enabled`. After every deployment run `nginx -t` followed by `systemctl reload nginx` to apply the changes.
