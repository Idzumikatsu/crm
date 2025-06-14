# Architecture Improvements

This document outlines planned enhancements for running the service on a single VPS. Items are reviewed quarterly and tracked via GitHub issues.

## Short term

- Consolidate deployment scripts into a dedicated directory.
- Configure an OpenTelemetry collector for tracing Spring Boot applications.
- Document how to provision the database using Terraform modules.

## Mid term

- Automate VPS provisioning with Ansible, including systemd units and NGINX configuration.
- Configure daily backups and log rotation on the server.

## Long term

- Evaluate breaking the monolith into discrete microservices.
- Adopt a service mesh for cross‑service authentication and observability.

