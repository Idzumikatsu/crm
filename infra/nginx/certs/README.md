This directory should contain `server.crt` and `server.key` for HTTPS termination.
In production copy the `fullchain.pem` and `privkey.pem` issued by Let's Encrypt
to these files. For local development you can generate a self-signed pair:

```bash
openssl req -x509 -newkey rsa:2048 -nodes -keyout server.key -out server.crt -days 365 -subj "/CN=localhost"
```
