# Security Checklist

Before rolling out NGINX to production, verify the following:

- TLS certificate validity and expiration dates
- HSTS header present and configured correctly
- ModSecurity with OWASP CRS is enabled
- Request rate limiting active with expected thresholds
- Request body size limits match documented values
- Access logs and error logs are shipping to the central system
- Sensitive information is not logged
- Backup of configuration and certificates is up to date

