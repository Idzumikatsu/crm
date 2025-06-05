# CONTEXT
I have a Maven-built Spring Boot 3.5.0 project that uses Flyway 11.9.1.
At runtime it connects to PostgreSQL 15.3 and fails with

    org.flywaydb.core.api.FlywayException: Unsupported Database: PostgreSQL 15.3

Current pom.xml contains only:
    <dependency>
        <groupId>org.flywaydb</groupId>
        <artifactId>flyway-core</artifactId>
        <version>11.9.1</version>
    </dependency>

# TASK
Update pom.xml so that Flyway recognises PostgreSQL 15.x.

# REQUIREMENTS
1. Add the database-specific module Flyway needs:
       <dependency>
           <groupId>org.flywaydb</groupId>
           <artifactId>flyway-database-postgresql</artifactId>
           <version>${flyway.version}</version>
       </dependency>
2. Keep flyway-core and flyway-database-postgresql on the **same** version (use the existing `${flyway.version}` property or introduce one if missing).
3. Ensure the Postgres JDBC driver is still present (postgresql-java 42.x or whatever BOM defines).
4. No other code changes – only dependency management.

# OUTPUT
Return the full corrected pom.xml snippet (within <dependencies>) plus a brief explanation (max 3 lines) of why the extra module is required.
