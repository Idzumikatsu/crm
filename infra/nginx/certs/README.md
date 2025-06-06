This directory should contain `server.crt` and `server.key` for HTTPS termination.
Use a trusted certificate in production or generate a self-signed pair for local testing:

```bash
openssl req -x509 -newkey rsa:2048 -nodes -keyout server.key -out server.crt -days 365 -subj "/CN=localhost"
```
