# ADR-001: Build Tool Choice

## Status
Accepted

## Context
Initially the project used Maven, but we migrated to Gradle with Kotlin DSL for faster incremental builds and a richer plugin ecosystem.

## Decision
Use **Gradle 8** with Kotlin DSL as the primary build system for the backend. The repository retains the Gradle Wrapper so builds are reproducible.

## Consequences
- CI workflows rely on `./gradlew` for all tasks.
- Developers must use Java 21 and Gradle 8 or the provided wrapper.
