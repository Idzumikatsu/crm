This directory receives `server.crt` and `server.key` used by NGINX.
During deployment certificates are obtained from Let's Encrypt
and copied here automatically. For local testing you may
generate a self-signed pair, for example:

```bash
openssl req -x509 -newkey rsa:2048 -nodes \
  -keyout server.key -out server.crt \
  -days 365 -subj "/CN=localhost"
```
