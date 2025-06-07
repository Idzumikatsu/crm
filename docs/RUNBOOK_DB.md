# Database Runbook

The application currently relies on the legacy schema (migrations V1–V3).
New migrations (V4+) introduce UUID-based tables that are not yet supported
by the Java code. To avoid startup errors, Flyway is configured to stop at
version **3**.

When initializing a new environment or recovering the database, use the
following procedure:

1. Start the application normally. Flyway will apply migrations up to V3.
2. Verify tables `users`, `teachers`, `students`, `groups`, `lessons`,
   `time_slots`, and others exist with **BIGINT** IDs.
3. If Flyway reports attempts to run migrations beyond V3, ensure
   `spring.flyway.target=3` is set in `application-postgres.yml` or
   `application-h2.yml`.

Once the codebase is updated for the UUID schema, remove the target limit
and migrate to newer versions.
