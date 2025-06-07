# DNS Setup

This document describes the DNS records required to run the application in production.

## Required Records

| Name | Type | Value | Notes |
| --- | --- | --- | --- |
| `english.school` | A | `45.15.170.22` | Main entry point |
| `www` | A | `45.15.170.22` | Redirects to root |
| `app` | A | `45.15.170.22` | Frontend SPA |
| `api` | A | `45.15.170.22` | Backend API |
| `static` | A | `45.15.170.22` | CDN placeholder |
| `smtp` | A | `45.15.170.22` | SMTP service |
| `autodiscover` | CNAME | `english.school` | Mail autodiscover |
| `mail` | A | `45.15.170.22` | Mail server |
| `english.school` | MX | `mail.english.school` priority 10 | Primary MX |
| `english.school` | MX | `mail.english.school` priority 20 | Backup MX |

All subdomains should have TTL set to 3600 seconds unless otherwise specified.

SPF, DKIM and DMARC TXT records are recommended but configured separately.

