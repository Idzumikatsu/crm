# AGENTS

> **Миссия агента:** *«При каждом коммите система становится лучше — чище, надёжнее, понятнее».*  
> Каждый агент — инженер уровня **top‑1 %**, который следует мировым best‑practices, пишет автотесты, документирует решения и отвечает за результат.

---

## 1 Назначение файла

`AGENTS.md` описывает **единые правила** разработки и сопровождения монорепозитория *CRM Calendar*.  
Это «соглашение о качестве» — если какое‑либо правило перестало работать, измените документ немедленно.

---

## 2 Технологический стек (синхронизирован с *Blueprint v0.4*)

| Слой / задача        | Технология / библиотека                        | Мин. версия | Назначение / примечание                                     |
|----------------------|------------------------------------------------|-------------|-------------------------------------------------------------|
| Build‑tool           | **Gradle Kotlin DSL**                          | 8.x         | Быстрые инкрементальные сборки, скрипты KTS без XML.         |
| Язык                 | **Java 21**                                    | 21 (LTS)    | Records, pattern‑matching → **без Lombok**.                  |
| Core‑Framework       | **Spring Boot 3.3.x**                          | 3.3         | Jakarta EE 10, Native image ready.                          |
| Объект‑маппинг       | **MapStruct 1.6**                              | 1.6         | Компиляция DTO ↔ Entity.                                    |
| Схема БД / миграции  | **Flyway 9.x**                                 | 9           | Версионируемые SQL‑скрипты для PostgreSQL.                  |
| БД (prod)            | **PostgreSQL 15**                              | 15          | GIN, `tstzrange`, partitioning.                             |
| БД (tests)           | Testcontainers Postgres 15 / H2 (`MEM`)        | 1.19        | Изолированные интеграционные тесты.                         |
| Аутентификация       | JWT (RS256) + TOTP 2FA                         | —           | Stateless, Telegram OTP fallback.                           |
| REST Doc             | SpringDoc OpenAPI‑Starter 2                    | 2           | Swagger UI → `/swagger-ui.html`.                            |
| Frontend             | **React 18 + Vite 5 + TypeScript 5**           | 18 / 5      | SPA в каталоге `frontend/`.                                 |
| UI kit / CSS         | **shadcn/ui + Tailwind CSS 3.4**               | 3.4         | Headless компоненты + утилитарные стили.                    |
| Календарь            | **FullCalendar 6** (`timeGrid` модуль)        | 6           | Drag‑n‑drop, ресурсы/оси.                                   |
| Контейнеризация      | Docker Compose (dev) → k3s + Helm (prod)       | 27 / 1.30   | Локальная среда ↔ кластер.                                  |
| CI/CD                | **GitHub Actions** → GHCR → Helm promote       | —           | Прямой pipeline из репозитория в prod.                      |
| Observability        | **Prometheus + Grafana + Loki**                | —           | Метрики, дашборды, логи в одном OSS‑стеки.                  |

> **Принцип обновлений:** security‑patch — немедленно; minor — раз в квартал; major — по плану.  

---

## 3 Архитектура

```
┌───────────────────────────┐
│       React SPA           │  (Vite, TS)
└───────────────────────────┘
             │ REST / JWT
┌───────────────────────────┐
│ Spring Boot API (hexagon) │
│  ├─ Web Layer (DTO)       │
│  ├─ Service Layer         │
│  ├─ Domain (Entities/VO)  │
│  └─ Persistence (JPA)     │
└───────────────────────────┘
             │
┌───────────────────────────┐
│ PostgreSQL 15             │
└───────────────────────────┘
```

* **Web‑layer** — контроллеры + валидация DTO.  
* **Service** — бизнес‑правила, транзакции.  
* **Domain** — модели с инвариантами, неизменяемые Value Objects.  
* **Persistence** — JPA + Flyway, запросы сложнее 3 `JOIN` → native SQL.  

---

## 4 Workflow разработки

1. **Понимание** задачи → читаем код, `Blueprint`, ADR, тесты.  
2. **Design Doc** (если > 30 мин) публикуем в Discussion или Draft PR.  
3. **Ветка**: `feature/<issue-id>-slug`, `bugfix/…`, `chore/…`.  
4. **TDD**: `red → green → refactor`, target: lines ≥ 90 %, branches ≥ 80 %.  
5. **Коммиты**: Conventional Commits (`feat:`, `fix:`, `docs:` …).  
6. **Pull Request** в `main`: описание что/почему + чек‑лист.  
7. **Review**: ≥ 1 approval + зелёный CI; замечания — шанс улучшить.  
8. **Squash & Merge** → GitHub Actions (build → docker → deploy Helm).  
9. **Tag** semver (`v0.4.0`) + CHANGELOG.

---

## 5 Стандарты кода (Java)

* **Стиль** — Google Java Style, enforced Spotless (`./gradlew spotlessCheck`).  
* **Null‑safety** — `@NonNull`, `Optional`, никаких «магических» `null`.  
* **Records** вместо Lombok.  
* **Логирование** — SLF4J + Logback, `DEBUG`/`INFO`; никаких `printStackTrace()`.  
* **Ошибка‑handling** — checked исключения = часть контракта; `@ControllerAdvice` для REST.  
* **Security** — Spring Security 6, BCrypt 12, `@PreAuthorize`.  
* **Performance** — избегать N+1; сложные запросы = `@Query(nativeQuery = true)`.  

### Frontend

* ESLint + Prettier (`npm run lint` в CI).  
* React functional components, hooks.  
* TanStack Query для data‑fetch.  
* Cypress e2e (`npm run e2e`).  

---

## 6 Тестирование

| Уровень        | Инструменты                                    | Цель                                  |
| -------------- | ---------------------------------------------- | ------------------------------------- |
| Unit (Java)    | JUnit 5 + Mockito                              | 80 % бизнес‑логики                    |
| Integration    | Testcontainers PostgreSQL 15                   | CRUD, пересечение тайм‑слотов         |
| Contract /API  | Spring MockMvc + Rest‑Assured                  | Основные REST‑флоу                    |
| E2E (web)      | **Cypress**                                    | Букинг, редактирование шаблонов       |
| Load           | k6                                             | p95 < 250 мс @ 2k RPS read            |
| Security       | OWASP ZAP docker                               | Ночные сканы, 0 blocker‑issues        |

---

## 7 CI/CD Pipeline (core шаги)

1. **backend**: `./gradlew test bootBuildImage`.  
2. **frontend**: `npm ci && npm run build` → nginx runtime image.  
3. **deploy**: Helm chart → k3s cluster; rollback = `helm rollback crm <rev>`.

Полная конфигурация — в `/.github/workflows/build-test-deploy.yaml` (*см. Blueprint*).

---

## 8 Чек‑листы

### Перед коммитом

- [ ] Локальные тесты `./gradlew test` / `npm test` зелёные.  
- [ ] Нет `TODO/FIXME` без ссылки на Issue.  
- [ ] Spotless / ESLint без ошибок.  

### Перед PR

- [ ] CI **зелёный** (`backend`, `frontend`).  
- [ ] Описаны *что* и *почему* изменено.  
- [ ] Обновлена документация / миграции.  
- [ ] Нет секретов в diff.  

### Перед релизом

- [ ] CHANGELOG обновлён, semver‑тег создан.  
- [ ] Миграции прошли на staging.  
- [ ] k6 / Cypress smoke — OK.  
- [ ] Бэкап БД сохранён.  

---

## 9 Скрипты

| Скрипт                     | Назначение                                      |
|----------------------------|-------------------------------------------------|
| `scripts/start-dev.sh`     | Запуск `docker‑compose.dev.yml`.                |
| `scripts/wait-for-db.sh`   | Ожидание Postgres в контейнере.                 |
| `scripts/setup-proxy.sh`   | Настройка Gradle / npm под корпоративный proxy. |

---

## 10 Как обновить AGENTS.md

Увидели расхождение с **Blueprint v0.4** или кодом?  
Откройте Issue с лейблом `doc` **или** пришлите PR → быстрый review + merge.

---

*Документ синхронизирован: 06 Jun 2025, соответствие проверено по **CRM Calendar – Implementation Blueprint v0.4***  
