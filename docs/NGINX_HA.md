# NGINX High Availability

## 8.1 Number of instances
The setup runs a **single** NGINX process. No external load balancer or
horizontal scaling is used.

## 8.2 Backend health checks
Backends are configured in the `upstream` block with `max_fails=3` and
`fail_timeout=30s`. Combined with `proxy_next_upstream` this removes unhealthy
servers from rotation until they recover.

## 8.3 Configuration reload
To reload configuration without dropping connections send the HUP signal to the
running process:


```bash
sudo systemctl kill -s HUP nginx
```

or execute `nginx -s reload`. Existing connections continue
until complete while workers load the new config.

## 8.4 Disaster recovery
Configuration files and TLS certificates are backed up daily. In case of a
complete failure NGINX can be redeployed from version control within **15
minutes** (RTO 15m). Backups ensure no more than **15 minutes** of configuration
changes can be lost (RPO 15m).
