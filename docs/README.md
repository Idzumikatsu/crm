# NGINX Documentation

This directory contains all NGINX related documents. Each file focuses on a specific aspect of the setup.

- `NGINX_REQUIREMENTS.md` – gathered requirements and assumptions.
- `NGINX_DESIGN.md` – target architecture and deployment model.
- `NGINX_HA.md` – high availability and disaster recovery plan.
- `NGINX_DEPLOYMENT.md` – CI/CD pipeline and delivery process.
- `SECURITY_CHECKLIST.md` – release checklist covering security controls.

Additional files describe the Kubernetes migration:
- `KUBERNETES_DESIGN.md` – target cluster architecture and stack.
- `KUBERNETES_DEPLOYMENT.md` – CI workflow and Helm usage.
- `RUNBOOK_K8S.md` – troubleshooting steps for the cluster.
- `NGINX_TRAINING.md` – training materials for developers and SREs.
- `DNS_SETUP.md` – required DNS records for production deployment.
- `SMTP_CONFIGURATION.md` – environment variables for outgoing email
- `KUBERNETES_DESIGN.md` – migration plan and target stack
- `KUBERNETES_DEPLOYMENT.md` – CI workflow for Helm-based releases
- `../infra/dns` – BIND zone files reflecting the recommended records.

Documentation is reviewed **quarterly**. The SRE Lead tracks outdated sections and opens issues for updates.
