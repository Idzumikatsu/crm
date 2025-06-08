# Proposed Architecture Improvements

This document summarises the current state of the monorepo and proposes steps to simplify development and increase deployment stability.

## Current structure

- **backend/** – Spring Boot application built with Gradle
- **frontend/** – React SPA using Vite
- **infra/** – Docker Compose with PostgreSQL, the app container and NGINX proxy
- **docs/** – design documents and runbooks

CI builds the backend and frontend, then deploys to a VPS via `docker compose`.
The Dockerfile expects a pre-built `app.jar` copied into the repository root.

## Pain points

- Manual `gradle build` and copying of `app.jar` before running Compose.
- Container `infra-app-1` restarts when the JAR is missing or the database is unavailable.
- Environment variables are set by hand for every run.
- No health checks in Compose, so failures are detected late.

## Suggested changes

1. **Multi-stage Docker build**
   - Let the Dockerfile build the JAR inside the image. This removes the manual `gradle build` step and guarantees that the correct artifact is packaged.
2. **.env file for compose**
   - Store variables like `SPRING_PROFILES_ACTIVE`, `DB_HOST` and `JWT_SECRET` in an `.env` file. Compose will pick them up automatically, simplifying local runs.
3. **Health checks**
   - Add `healthcheck` instructions for the app and database containers. Restart policies can then rely on container health instead of repeated crashes.
4. **docker compose profiles**
   - Separate dev and prod options via Compose profiles. Development would mount source code volumes, while production builds a slim image.
5. **Makefile helpers**
   - Provide `make build` and `make up` targets that wrap the common steps (building images, running Compose, tailing logs).
6. **Logging and troubleshooting**
   - Document how to inspect `docker compose logs app` and typical startup issues. Include this in the runbook.

## Action plan

1. Update `backend/Dockerfile` to use a multi-stage build (Gradle image -> JRE image).
2. Create `infra/.env.example` with sane defaults. Developers copy it to `infra/.env` and adjust secrets.
3. Extend `infra/docker-compose.yml` with `healthcheck` entries and use variables from `.env`.
4. Add a short Makefile in the repository root with `build`, `up` and `down` targets.
5. Update `README.md` with the simplified setup instructions.
6. Verify that `docker compose up --build` starts all containers without restarts.

These steps should reduce configuration errors and speed up deployments.
