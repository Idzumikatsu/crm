This directory should contain `fullchain.pem` and `crm-synergy.key` for HTTPS termination.
Use the certificate bundle provided by your CA together with the matching private key.
On CI the files are created from the `SSL_CERT` and `SSL_KEY` secrets. Store the
output of `base64 -w0` for `fullchain.pem` and `crm-synergy.key` in those
variables.
To test locally you can generate a self-signed pair:

```bash
openssl req -x509 -newkey rsa:2048 -nodes -keyout crm-synergy.key -out fullchain.pem -days 365 -subj "/CN=localhost"
```
