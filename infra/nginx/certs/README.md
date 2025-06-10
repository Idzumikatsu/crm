This directory should contain `crm-synergy.crt` and `crm-synergy.key` for HTTPS termination.
Use the `fullchain.pem` and `privkey.pem` issued by Let's Encrypt for production deployments.
On CI the files are created from the `SSL_CERT` and `SSL_KEY` secrets. Store the
output of `base64 -w0` for `crm-synergy.crt` and `crm-synergy.key` in those
variables.
To test locally you can generate a self-signed pair:

```bash
openssl req -x509 -newkey rsa:2048 -nodes -keyout crm-synergy.key -out crm-synergy.crt -days 365 -subj "/CN=localhost"
```
