# TODO · High‑Priority Polish & Production‑Readiness (07 Jun 2025)

> **Цель фазы «Polish»** — превратить MVP в полноценный удобный сервис: законченный UI/UX, Production‑grade уведомления, чистый бренд‑дизайн, безопасный DNS + SSL.
> **Приоритет:** выше, чем задачи пост‑MVP roadmap; эти работы выполняются **сразу** после стабилизации MVP.

| P      | ID         | Задача                                                                                     | Est | Зависимость  |
| ------ | ---------- | ------------------------------------------------------------------------------------------ | --- | ------------ |
| **P0** | **DNS‑1**  | Перенести DNS зоны (`crm‑synergy.io`) на **Cloudflare**                                    | 1 h | —            |
| P0     | **DNS‑2**  | Настроить A/CNAME: `app`, `api`, `static`, `smtp`, `autodiscover`                          | 1 h | DNS‑1        |
| P0     | **DNS‑3**  | Выпустить TLS certы Let’s Encrypt (Cloudflare DNS‑01) + HSTS preload                       | 1 h | DNS‑2        |
| **P0** | **NT‑1**   | Production SMTP: Mailgun sandbox → verified domain, SPF, DKIM, DMARC                       | 2 h | DNS‑2        |
| P0     | **NT‑2**   | Telegram Bot: зарегистрировать `@SynergyCRMBot`, включить webhook `/tg/callback`           | 2 h | —            |
| P0     | **DATA‑1** | Расширить `student` таблицу: `email NOT NULL`, `telegram_username`, `valid_contact = bool` | 1 h | —            |
| **P0** | **UX‑1**   | Создать **Figma** файл c полным визуалом (лого, палитра, типографика)                      | 4 h | —            |
| P0     | **UX‑2**   | Имплементировать global дизайн‑system: Tailwind config + `shadcn/ui` theme tokens          | 3 h | UX‑1         |
| P0     | **UX‑3**   | Pixel‑perfect макеты: Login, Dashboard, Calendar, Settings (RU+EN)                         | 6 h | UX‑2         |
| **P1** | **NT‑3**   | UI редактор шаблонов: e‑mail + TG с превью, переменные подсвечены                          | 4 h | NT‑1, NT‑2   |
| P1     | **NT‑4**   | Пользовательские настройки уведомлений (каналы + расписание)                               | 3 h | NT‑3         |
| P1     | **UX‑4**   | Добавить skeleton‑загрузки, анимации переходов (Framer Motion)                             | 2 h | UX‑3         |
| P1     | **UX‑5**   | Accessibility audit — WCAG AA: контрасты, ARIA, key‑nav                                    | 3 h | UX‑3         |
| **P1** | **ONB‑1**  | Wizard‑онбординг педагога: загрузка аватара, установка буфера, выбор шаблонов              | 4 h | UX‑3, DATA‑1 |
| P1     | **ONB‑2**  | Справка «Quick‑start для менеджера» в приложении (markdown modal)                          | 2 h | UX‑3         |
| P1     | **DATA‑2** | Миграция: backfill email/telegram у существующих students; отчёт missing                   | 2 h | DATA‑1       |
| **P2** | **BR‑1**   | Анимация логотипа (SVG) на landing                                                         | 1 h | UX‑1         |
| P2     | **QOS‑1**  | Интегрировать **Sentry** release tracking FE+BE                                            | 2 h | DNS‑3        |
| P2     | **QOS‑2**  | GH Actions smoke‑test в Prod (check /health, /api/v1/info)                                 | 1 h | DNS‑3        |

### Легенда приоритетов

* **P0** — блокирует выход в публичное продакшн‑использование.
* **P1** — критично для качественного UX и снижения поддержки.
* **P2** — улучшает имидж и наблюдаемость, не блокирует релиз.

\*Файл обновлён: 07 Jun 2025 • Синхронизирован с кодовой базой \**`main@HEAD`*


# TODO · Post-MVP Roadmap (Q3 2025)

Ниже — задачи, направленные на развитие CRM-календаря после успешного релиза MVP.  
Каждая задача снабжена уникальным ID, приоритом (P1-P3) и оценкой (ideal-hours).  
**Не** включены уже реализованные возможности.

---

## EPIC 1 · Групповые занятия (P1)

| ID | Задача | Est | Зависимость |
|----|--------|-----|-------------|
| **GL-1** | Расширить схему: `lesson_participant` (lesson_id, student_id, role) | 4 h | — |
| **GL-2** | Обновить API `/lessons` (POST/PATCH) → массив `studentIds[]` | 3 h | GL-1 |
| **GL-3** | UI: модалка выбора нескольких студентов, бейдж «Group» в календаре | 6 h | GL-2 |
| **GL-4** | Пересчёт оплаты: опция «фикс ставка ± коэф. за группу» | 3 h | GL-1 |

---

## EPIC 2 · Самозапись ученика (P1)

| ID | Задача | Est | Зависимость |
|----|--------|-----|-------------|
| **SB-1** | Публичный read-only виджет расписания преподавателя (`/book/{teacher}`) | 5 h | — |
| **SB-2** | Разрешить гостю бронировать свободный слот → создаётся `Student` + `Lesson` (статус REQUESTED) | 6 h | SB-1 |
| **SB-3** | Экран «Вход по коду» (одноразовый токен из e-mail/TG) для просмотра своих уроков | 4 h | SB-2 |
| **SB-4** | Настройки: включить/выключить самозапись, макс. кол-во уроков в день | 2 h | SB-1 |

---

## EPIC 3 · Платежи (P2)

| ID | Задача | Est | Зависимость |
|----|--------|-----|-------------|
| **PY-1** | Интеграция YooKassa (v2 Payments API) — создание счёта при брони | 6 h | SB-2 |
| **PY-2** | Webhook `/payments/callback` — пометка `lesson.paid_at` | 3 h | PY-1 |
| **PY-3** | Отчёт «Начисления → Выплаты преподавателям» | 4 h | PY-2 |

---

## EPIC 4 · 2-way Google Calendar Sync (P2)

| ID | Задача | Est | Зависимость |
|----|--------|-----|-------------|
| **GC-1** | OAuth flow для преподавателя, сохранение refresh-token | 4 h | — |
| **GC-2** | Export уроков в Google Calendar (Insert/Update/Delete) | 5 h | GC-1 |
| **GC-3** | Polling/Push-вебхуки – импорт внешних событий как `BLOCKED` TimeSlot | 6 h | GC-1 |

---

## EPIC 5 · Мобильный PWA (P3)

| ID | Задача | Est | Зависимость |
|----|--------|-----|-------------|
| **PWA-1** | ServiceWorker + offline-cache статики | 3 h | — |
| **PWA-2** | Manifest (icon-set, splash-screens) | 2 h | PWA-1 |
| **PWA-3** | Push-уведомления (Web Push) об изменениях уроков | 5 h | PWA-1 |

---

## EPIC 6 · Об Observability (P2)

| ID | Задача | Est | Зависимость |
|----|--------|-----|-------------|
| **OB-1** | Включить OpenTelemetry tracing (Spring Micrometer OTel) | 4 h | — |
| **OB-2** | Экспорт трасс в Jaeger, дашборд «slow query» | 3 h | OB-1 |
| **OB-3** | Alert Rules: P95 > 400 ms, error-rate > 2 % | 2 h | OB-2 |

---

## EPIC 7 · AI-подсказки (P3)

| ID | Задача | Est | Зависимость |
|----|--------|-----|-------------|
| **AI-1** | Модель «best-slot» — рекомендация времени на основе истории ученика | 6 h | GC-3, OB-2 |
| **AI-2** | UI «Suggest slot» кнопка в Lesson Modal | 3 h | AI-1 |

---

## Служебные задачи

| ID | Задача | Est |
|----|--------|-----|
| **DOC-1** | Обновить Swagger → OpenAPI 3.2, добавить group-lessons схемы | 2 h |
| **CI-1**  | Matrix-build: JDK 21 + 22-ea, Node 20 + 22 | 2 h |
| **DEV-1** | Helm chart values-templates per environment (dev/stg/prod) | 3 h |

---

### Приоритеты

* **P1** — критично для K-фактор роста / дохода.  
* **P2** — повышает ценность, но не блокирует основную работу.  
* **P3** — исследовательское / nice-to-have.

---

> **Всего:** 16 главных задач, 72 ideal-hours.  
> Обновление: 07 Jun 2025 — синхронизировано с текущим кодом `main@HEAD`.
