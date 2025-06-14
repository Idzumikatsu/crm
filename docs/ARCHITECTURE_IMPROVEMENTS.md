# Architecture Improvements

This document collects future improvements for the overall project architecture and infrastructure. Items are reviewed quarterly and tracked via GitHub issues.

## Short term

- Move the Docker Compose setup to version 3 syntax and split services into separate files.
- Configure an OpenTelemetry collector for tracing Spring Boot applications.
- Document how to provision the database using Terraform modules.

## Mid term

- Package the application as OCI images for easier container deployment.
- Introduce a message broker (RabbitMQ or Kafka) for asynchronous workflows.

## Long term

- Evaluate breaking the monolith into discrete microservices.
- Adopt a service mesh for cross‑service authentication and observability.

