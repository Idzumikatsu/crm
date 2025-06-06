**CRM Calendar – Implementation Blueprint *v0.4 (Best‑Practice Refresh)***
*Last updated: 06 Jun 2025 – targets MVP go‑live 16 Jun 2025*

---

### 0  Philosophy & Key Technical Choices

| Layer              | Choice                                                 | Rationale                                                                                                      |
| ------------------ | ------------------------------------------------------ | -------------------------------------------------------------------------------------------------------------- |
| **Build**          | **Gradle Kotlin DSL**                                  | Fast incremental build, rich plugin ecosystem, Kotlin scripts > XML. We migrate repo from Maven (script BE‑0). |
| **Language**       | **Java 21** (records, pattern matching)                | Eliminates boilerplate → no Lombok needed.                                                                     |
| **Framework**      | **Spring Boot 3.3**                                    | Mature, native image ready, Jakarta EE 10.                                                                     |
| **DB**             | **PostgreSQL 15**                                      | Rich date/time ops (`tstzrange`), GIN, partitioning.                                                           |
| **Schema‑mgt**     | **Flyway 9**                                           | Simple versioned SQL, works well with Postgres features.                                                       |
| **Object‑mapping** | **MapStruct 1.6** + **Jakarta Records**                | Typesafe compile‑time mappers.                                                                                 |
| **Frontend**       | **React 18 + Vite + TypeScript 5**                     | Fast HMR, tree‑shaking, typed.                                                                                 |
| **UI lib**         | **shadcn/ui + Tailwind CSS 3.4**                       | Accessible headless primitives, fast design iterations.                                                        |
| **Calendar**       | **FullCalendar 6 — timeGrid**                          | Battle‑tested drag‑n‑drop.                                                                                     |
| **Auth**           | **JWT (RS256) + TOTP 2FA**                             | Stateless sessions; native support for Telegram OTP fallback.                                                  |
| **Infra**          | **Docker Compose (dev)** → **k3s Helm (prod)**         | Simple local dev; production in‑cluster scaling.                                                               |
| **CI/CD**          | **GitHub Actions** → Container registry → Helm promote | Ubiquitous, zero vendor lock‑in.                                                                               |
| **Observability**  | **Prometheus + Grafana + Loki**                        | OSS standard stack.                                                                                            |

---

## 1  Migration Plan (archive → best‑practice stack)

| ID       | Step                                                                               | Effort | Outcome                                                                        |
| -------- | ---------------------------------------------------------------------------------- | ------ | ------------------------------------------------------------------------------ |
| **BE‑0** | *Maven → Gradle* automated via `gradle init`, remove `pom.xml`, adjust GH Actions. | 3 h    | Builds green under Gradle Kotlin DSL. ✅ DONE                                          |
| **BE‑1** | Remove Lombok                | 5 h    | Replace with Java records / explicit constructors; `./mvnw` deleted. ✅ DONE |
| **BE-2** | Upgrade to Java 21 + Spring Boot 3.3 | 2 h | Module path, Jakarta packages. ✅ DONE |
| **BE‑3** | Introduce MapStruct mappers                | 2 h    | `*Mapper.java` for DTOs. ✅ DONE |
| **BE‑4** | TimeZone refactor                                                                  | 4 h    | Replace `LocalDateTime` with `OffsetDateTime`, store UTC; Flyway `V2__tz.sql`. |
| **FE‑0** | Scaffold React SPA in `frontend/`                | 1 h    | Vite dev server up. ✅ DONE |

---

## 2  Repository Layout (post‑migration)

```
crm-calendar/
 ├── backend/
 │    ├── build.gradle.kts          # root Gradle
 │    ├── settings.gradle.kts
 │    ├── src/main/java/com/synergycrm/...
 │    └── src/test/java/...
 ├── frontend/
 │    ├── package.json, vite.config.ts, tailwind.config.js
 │    └── src/...
 ├── infra/
 │    ├── docker-compose.dev.yml
 │    └── k8s/helm/
 └── docs/
      ├── ADR‑001_build‑tool.md
      └── ERD.png
```

---

## 3  Data Model (definitive)

```
user(id UUID, email, pwd, role, tz, locale, 2fa_secret, active, created_at)
teacher(id UUID PK‑FK→user, hourly_rate_cents, buffer_min)
student(id UUID, first_name, last_name, phone, telegram, email, notes)
availability_template(id UUID, teacher_id, weekday, start_time, end_time, until_date)
availability_exception(id UUID, template_id?, teacher_id, date, reason)
time_slot(id UUID, teacher_id, start_ts TIMESTAMPTZ, end_ts TIMESTAMPTZ,
          status OPEN|BOOKED|BLOCKED, lesson_id UUID?)
lesson(id UUID, teacher_id, student_id, start_ts, end_ts, duration_min,
       status SCHEDULED|COMPLETED|CANCELLED, price_cents, created_by, updated_by, updated_at)
notification_template(id UUID, code, lang, subject, body_html)
notification_log(id UUID, lesson_id, channel, sent_at, status, read_at, payload_json)
audit_log(id bigserial, entity, entity_id, action, old_json, new_json, actor_id, ts)
```

*GIST index*:
`CREATE INDEX time_slot_overlap ON time_slot USING GIST (tsrange(start_ts, end_ts));`

---

## 4  Backend Implementation Checklist

| # | Task                    | Owner | Est | Details                                                        |
| - | ----------------------- | ----- | --- | -------------------------------------------------------------- |
| 1 | **Availability module** | Codex | 6 h | Service: generate / delete slots; REST: `/templates`, `/slots` |
| 2 | **Booking service**     | Codex | 5 h | `LessonService.book(...)` with slot lock; 409 on conflict      |
| 3 | **Reminder engine**     | Codex | 3 h | Quartz per lesson; channels email/TG; payload merge            |
| 4 | **Analytics API**       | Codex | 4 h | Materialized view, POI XLSX export, Google Sheets push         |
| 5 | **Security 2FA**        | Codex | 3 h | TOTP secret provisioning, QR gen, login flow                   |
| 6 | **Audit log AOP**       | Codex | 2 h | `@Track` annotation → diff capture JSON-Patch                  |

*Code stubs for each delivered in `backend/src/main/java/...`*

---

## 5  Frontend Implementation Checklist

| # | Feature                      | Est | Key Components                                                  |
| - | ---------------------------- | --- | --------------------------------------------------------------- |
| 1 | Auth & layout                | 3 h | `<AuthGate/>`, `<Sidebar/>`, React Router v6                    |
| 2 | Teacher calendar             | 4 h | `<TeacherCalendar/>`, FullCalendar, availability template modal |
| 3 | Manager timeline             | 5 h | `<ManagerCalendar/>` with teacher selector, Lesson modal        |
| 4 | Student search & CRUD        | 2 h | `<StudentCombobox/>`, `<StudentDialog/>`                        |
| 5 | Settings (buffer, templates) | 3 h | `<SettingsPage/>`, TipTap editor                                |
| 6 | Analytics dashboard          | 3 h | React Query fetch → `<LineChart/>` (recharts)                   |

---

## 6  CI/CD (final)

```yaml
name: build-test-deploy
on:
  push:
    branches: [ main ]
jobs:
  backend:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-java@v4
        with: { java-version: '21', distribution: 'temurin' }
      - uses: gradle/gradle-build-action@v3
      - run: ./gradlew test bootBuildImage --imageName=ghcr.io/org/crm-backend:${{ github.sha }}
  frontend:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-node@v4
        with: { node-version: '20' }
      - run: |
          cd frontend
          npm ci
          npm run build
      - uses: docker/build-push-action@v5
        with:
          context: frontend
          file: infra/Dockerfile.nginx
          tags: ghcr.io/org/crm-frontend:${{ github.sha }}
  deploy:
    needs: [backend, frontend]
    runs-on: ubuntu-latest
    steps:
      - name: Helm promote
        run: helm upgrade --install crm ./infra/k8s -f infra/k8s/values-prod.yaml
```

---

## 7  Test Strategy

| Layer       | Tool                          | Coverage target                 |
| ----------- | ----------------------------- | ------------------------------- |
| Unit        | JUnit5 + Mockito              | 80 % business modules           |
| Integration | Testcontainers Postgres       | CRUD, overlap guard             |
| API         | Spring MockMvc + Rest‑Assured | Critical flows                  |
| E2E         | Cypress                       | Booking scenario, template edit |
| Load        | k6 (HTTP)                     | p95 < 250 ms @ 2k RPS read      |
| Security    | OWASP ZAP docker              | nightly scan, 0 blocker issues  |

---

## 8  Sprint Timeline (calendar‑days)

| Day | Deliverable                       | Notes                    |
| --- | --------------------------------- | ------------------------ |
| 1   | Repo migration (BE‑0, BE‑1)       | Green build Gradle + J21 |
| 2‑3 | Availability module (BE‑2, FE‑2)  | Teacher calendar ready   |
| 4‑5 | Booking flow (BE‑3, FE‑3)         | Manager timeline live    |
| 6   | Reminder + 2FA (BE‑4, FE‑1 tweak) | E‑mails/TG sending       |
| 7   | Analytics + export (BE‑5, FE‑6)   | XLSX download works      |
| 8   | Hardening, tests, ZAP             | 80 % coverage            |
| 9   | Load test, k3s deploy             | p95 latency check        |
| 10  | Smoke + stakeholder review        | MVP tag v0.1 prod        |

---

## 9  Open Points to Confirm

1. Hourly rate currency = **RUB**? if multi‑currency needed, add `currency` column in `teacher`.
2. Data retention: propose 3 years for `audit_log`, 1 year for `notification_log`.
3. Provide sample RU/EN templates by 09 Jun for Codex seeding.

---

*End of Blueprint v0.4 – ready for Codex execution*
