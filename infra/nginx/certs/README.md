This directory should contain `server.crt` and `server.key` for HTTPS termination.
Provide your real certificate and key in production. For local testing you may
generate a self-signed pair, for example:

```bash
openssl req -x509 -newkey rsa:2048 -nodes \
  -keyout server.key -out server.crt \
  -days 365 -subj "/CN=localhost"
```
