# AGENTS · Post-MVP Edition (07 Jun 2025)

> **Миссия агента:** «С каждым коммитом система становится _надёжнее, быстрее и полезнее_».

Документ обновлён после завершения **MVP (Blueprint v0.4)**  
и фиксирует правила разработки для следующего этапа.

---

## 1 Статус проекта

| Слой          | MVP Done | Примечание |
|---------------|:-------:|------------|
| Backend API   | ✅ | Spring Boot 3.3, Java 21, JWT + TOTP |
| Frontend SPA  | ✅ | React + Vite, FullCalendar |
| CI/CD         | ✅ | GitHub Actions → k3s Helm |
| Notifications | ✅ | e-mail / Telegram, шаблоны RU+EN |
| Analytics v0  | ✅ | XLSX + Google Sheets export |
| **Next**      | — | см. TODO.md |

---

## 2 Текущий стек

(Не изменился: Gradle Kotlin DSL, MapStruct 1.6, PostgreSQL 15, Tailwind CSS 3.4, Prometheus-stack.)

---

## 3 Branch & Release Flow

* `main` → production tag & helm deploy  
* `develop` → auto-deploy staging  
* `feat/<topic>` → PR ↦ `develop` (review ≥ 1)  
* **Semantic Release:** `fix`, `feat`, `perf`, `ci`, `doc`, `refactor`, `test`, `chore`.

---

## 4 Качество кода

* **Java 21**: records + pattern matching → _нет Lombok_.  
* **Frontend**: ESLint Airbnb + Prettier, Husky hooks.  
* **Coverage**: ≥ 80 % backend, ≥ 70 % frontend (Cypress E2E).  
* **Static Analysis**: `spotlessCheck`, `sonar:scan`.  
* **Sentry** подключён для FE+BE (см. infra/helm/sentry).  

---

## 5 Security & Compliance

* OWASP Top-10 hardening; nightly ZAP scan.  
* GDPR endpoints `/me/export` & `/me/delete` готовы.  
* Data retention policy (draft):  
  * `audit_log` – 3 года; `notification_log` – 1 год.  

---

## 6 Как обновлять этот файл

PR c лейблом `doc` или изменение, связанное с появлением/удалением правила в CI / коде.  
После слияния — обязательно bump версии шапки.

*Документ синхронизирован с исходным кодом на 07 Jun 2025.*
