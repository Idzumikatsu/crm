This directory should contain TLS materials for NGINX:

* `crm-synergy.key` – the private key
* `crm-synergy.crt` – the leaf certificate
* `crm-synergy_ca.crt` – the intermediate certificate
* `fullchain.pem` – concatenation of `crm-synergy.crt` and `crm-synergy_ca.crt`

The CI workflow restores these files from the `SSL_CERT`, `SSL_CA_CERT` and
`SSL_KEY` secrets. The values may be stored either as PEM text or Base64
encoded – the workflow detects the format automatically.

Use the certificates issued by Let's Encrypt in production. For local tests you
can generate a self-signed pair:

```bash
openssl req -x509 -newkey rsa:2048 -nodes -keyout crm-synergy.key -out crm-synergy.crt -days 365 -subj "/CN=localhost"
```
