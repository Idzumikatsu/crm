# AGENTS

> **Кредо агента:** «Не просто писать код, а улучшать систему при каждом изменении». Каждый агент — инженер уровня *top‑1 %*, стремящийся к безупречному качеству, прозрачности и надёжности.

---

## 1. Назначение файла

`AGENTS.md` задаёт **единообразные правила** для всех, кто вносит изменения в репозиторий. Здесь собраны:

* требования к коду, тестам и документации;
* архитектурные принципы и правила безопасности;
* стандарты CI/CD и выпуска версий;
* чек‑листы качества — независимо от персоналий.

Документ должен оставаться **коротким, но исчерпывающим**. Обнаружили устаревшее правило — исправляйте сразу.

---

## 2. Технологический стек проекта

| Слой / задача          | Технология / библиотека                        | Мин. версия | Назначение / комментарий                                        |
|------------------------|------------------------------------------------|-------------|-----------------------------------------------------------------|
| Язык                   | **Java 21 LTS**                                | 21          | Loom, record‑классы, pattern‑matching.                          |
| Сборка                 | **Maven 3.9.9** (`./mvnw`)                    | 3.9         | Reproducible builds, Enforcer, Wrapper.                         |
| Core‑Framework         | **Spring Boot 3.5.x**                         | 3.5         | Актуальный LTS, AOT, CRaC‑ready.                                |
| SSR / шаблоны          | **Thymeleaf 3.2** + Extras Security 6          | —           | Рендеринг UI на сервере.                                        |
| REST‑документация      | SpringDoc OpenAPI‑Starter 2.8.x               | —           | Swagger UI на `/swagger-ui.html`.                               |
| Валидация              | Jakarta Bean Validation 3                     | —           | Проверка DTO и форм.                                            |
| Persistency            | Spring Data JPA + Hibernate 6                 | —           | ORM.                                                            |
| Миграции схемы         | **Flyway 11.9.1**                             | 11.9        | Контролируемая эволюция БД.                                     |
| БД продакшн            | **PostgreSQL 15.3**                           | 15          | Основное хранилище.                                             |
| БД dev / тесты         | **H2 2.x** (in‑mem)                           | 2           | Быстрые изолированные тесты.                                    |
| Аутентификация JWT     | **JJWT 0.12.6**                               | —           | Access / refresh‑токены, `HS256`.                               |
| CSS‑фреймворк          | Tailwind CSS (via Node **18 LTS+**)            | 18+         | Адаптивная вёрстка, `npm run build`.                            |
| UI‑тесты (E2E)         | **Selenium 4.13** + HtmlUnit Driver           | 4.13        | Headless‑браузерные сценарии.                                   |
| Контейнеризация        | Docker 27.5 + Compose                         | 27          | Прод‑ и dev‑среда едины.                                        |
| Обратный прокси        | **NGINX 1.26** (контейнер) + Prom exporter     | —           | TLS‑терминация, статический контент, метрики Nginx.             |
| CI/CD                  | GitHub Actions                                | —           | Build → Test → Docker publish → Deploy VPS.                     |

> **Обновления:** патчи безопасности — немедленно; минорные версии — не реже одного раза в квартал.

---

## 3. Архитектурные принципы

```
┌───────────────┐     ┌────────────────┐
│  REST + SSR   │ ←→ │  Controllers   │  (DTO / ViewModels)
└───────────────┘     └────────────────┘
          ↓ Service layer
┌───────────────────────────────────────┐
│               Domain                 │  (Entities, VOs, Policies)
└───────────────────────────────────────┘
          ↓ Infrastructure
┌──────────────┐     ┌────────────────┐
│ Repositories │     │ Integrations   │  (external REST, SMTP …)
└──────────────┘     └────────────────┘
```

* **Контроллеры:** минимум логики — валидация, маппинг DTO ↔ Domain, выбор view.
* **Сервисы:** чистая бизнес‑логика, не зависящая от Spring.
* **Domain:** неизменяемые Value Objects, инварианты внутри агрегатов.
* **Infrastructure:** адаптеры БД, файлов, внешних API. Общение через интерфейсы.

---

## 4. Workflow разработки

1. **Понимание задачи** — прочтите код, схемы, `README.md` и тесты.
2. **Design Doc** (если > 30 мин) — Discussion или draft PR.
3. **Ветка**: `feature/<issue-id>-<slug>`, `bugfix/…`, `hotfix/…`, `chore/…`.
4. **TDD**: `Red → Green → Refactor`, покрытие: строки ≥ 90 %, ветви ≥ 80 %.
5. **Коммиты** — Conventional Commits (`feat:`, `fix:`, `docs:` …).
6. **Pull Request** в `main`: описание *что* сделано и *почему*, ссылка на задачу.
7. **Review**: ≥ 1 approval + зелёный CI. Комментарии — шанс улучшить код.
8. **Squash & Merge**: история чистая, один коммит описывает изменение.
9. **Deploy**: GitHub Actions собирает образ, заливает на VPS, перезапускает `docker compose`.

---

## 5. Стандарты кода

* **Стиль:** Google Java Style + Spotless (`mvn spotless:check` в CI).
* **Null‑safety:** `@NonNull`, `Optional` — никаких необъяснимых `null`.
* **Lombok:** `@Builder`, `@Value` — **только** для DTO и конфигураций.
* **Логирование:** SLF4J + Logback; `DEBUG` в dev, `INFO` в prod; без `System.out`.
* **Обработка ошибок:** checked‑исключения — часть контракта; `@ControllerAdvice` для REST.
* **Security:** Spring Security 6, BCrypt 12; `@PreAuthorize` на публичных методах сервиса.
* **Performance:** избегать N+1; сложные запросы — `@Query(nativeQuery = true)` или `@EntityGraph`.

---

## 6. Тестирование

| Уровень          | Инструменты                                           | Особенности                                  |
|------------------|-------------------------------------------------------|---------------------------------------------|
| Unit             | JUnit 5, Mockito                                      | Given‑When‑Then, 100 % isolation.           |
| Integration      | Spring Boot Test, H2 in‑mem                           | Профиль `h2`, миграции Flyway перед стартом |
| API (REST)       | MockMvc, RestAssured, Spring‑Security‑Test            | Статусы, схемы, headers.                    |
| UI (E2E)         | **Selenium 4** + HtmlUnit driver                      | Headless, tag `@e2e`, запуск по флагу.      |
| Coverage         | JaCoCo 0.8.13                                         | Отчёт публикует CI.                         |

> **Примечание:** Testcontainers пока не используется; добавим при переходе на интеграционные тесты с PostgreSQL.

---

## 7. Документация

* **Javadoc** — на публичные API.
* **README.md** — bootstrap проекта.
* **Swagger UI** — `http://<host>/swagger-ui.html`.
* **Диаграммы** — `docs/` (PlantUML / Mermaid).
* **CHANGELOG.md** — формат *Keep a Changelog*, версия = Git‑тег.

---

## 8. Безопасность

* `mvn org.owasp:dependency-check-maven:check` — CVE‑скан.
* Секреты — только GitHub Secrets (CI) или AWS Parameter Store (prod).
* Все входные данные валидируются Bean Validation.
* HTTPS‑терминация — Nginx (конфиг `nginx/nginx.conf`).

---

## 9. Обсервабилити

* **NGINX Prometheus Exporter** публикует метрики на `/metrics` (порт 9113).  
* Мониторинг приложения (Micrometer / Actuator) — **roadmap**.

---

## 10. Деплой

* **`main`** — единственный prod‑бранч.
* CI: `mvn clean package` → Docker build → push `ghcr.io/...` → SSH‑deploy VPS.
* Rollback: `docker compose ls` → `docker compose up -d app:<prev_tag>`.

---

## 11. Чек‑листы

### Перед коммитом

- [ ] Убедитесь, что `./mvnw test` локально **зелёный**.
- [ ] Форматирование + Spotless OK.
- [ ] Нет `TODO/FIXME` без ссылки на issue.
- [ ] Commit message — Conventional Commit, на английском.

### Перед Pull Request

- [ ] Все проверки CI **зелёные**.
- [ ] Описано *что* и *почему* изменено.
- [ ] Обновлена документация / схемы / миграции.
- [ ] Нет секретов или конфиденциальных данных в diff.

### Перед релизом

- [ ] CHANGELOG обновлён, версия — semver tag.
- [ ] Миграции прошли на staging.
- [ ] Smoke‑тесты (UI + REST) зелёные.
- [ ] Создана резервная копия БД.

---

## 12. Служебные скрипты

| Скрипт               | Назначение                                             |
|----------------------|---------------------------------------------------------|
| `start.sh`           | Локальный запуск (Maven Wrapper).                       |
| `wait-for-db.sh`     | Ожидание готовности Postgres перед стартом приложения.  |
| `scripts/setup-proxy.sh` | Настройка Maven/Git под корпоративный proxy.       |

---

## 13. Как улучшить этот документ

Нашли несоответствие или идею — откройте Issue с лейблом `doc` **или** создайте PR.  
Документ живёт, пока мы его улучшаем.
