# Schedule Tracker

Монорепозиторий включает backend на Spring Boot и SPA на React.

## Структура проекта

- `backend/` — серверная часть и REST API
- `frontend/` — клиентское приложение Vite/React
- `infra/` — Docker Compose, Helm charts и конфигурация NGINX
- `docs/` — документация и ADR. Также в каталоге находится файл
  `ARCHITECTURE_IMPROVEMENTS.md` с планом дальнейшего развития
  инфраструктуры и рекомендациями по запуску

## Настройка окружения

1. **Java 21**
   - Установите JDK 21. На Linux можно установить пакет `openjdk-21-jdk`.
2. **Gradle**
   - Используйте Gradle 8.14 или запускайте через прилагаемый скрипт `./backend/gradlew`.
3. **Git hooks**
   - Для автoформатирования Java-кода выполните `git config core.hooksPath scripts/git-hooks`.
   - При коммите будет запускаться `./backend/gradlew spotlessApply`.
4. **Node.js**
   - Use Node 20 or 22 as in the CI matrix.
  - Run `npm install` (or `npm ci`) before any npm script such as `npm run build` or `npm run lint`.
   - После `npm install` запустите `cd frontend && npm run lint`, чтобы проверить стиль кода.
   - Большинство ошибок форматирования исправляется автоматически
     через `cd frontend && npm run lint:fix`.
5. **PostgreSQL**
   - Приложение ожидает базу `schedule` с пользователем `postgres` и паролем `postgres`.
   - Чтобы быстро запустить базу через Docker, выполните:
     ```bash
     docker run --name schedule-db -p 5432:5432 -e POSTGRES_DB=schedule \
      -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -d postgres:16.2
     ```
    - В `application.yml` адрес БД задаётся через переменную окружения `DB_HOST`.
      По умолчанию используется `db` (можно переопределить). Основной профиль
      `postgres` рассчитан на PostgreSQL. Для тестов можно включить профиль
      `dev`, который использует встроенную базу H2.
    - Для миграций используется Flyway **11.9.1**, что обеспечивает поддержку
      PostgreSQL 16.2.
6. **Сборка и запуск backend**
   - Сборка: `./backend/gradlew build`
   - Запуск приложения: `./backend/gradlew bootRun`
   - Приложение слушает порт `8080`. Убедитесь, что этот порт свободен
     перед запуском, иначе старт завершится ошибкой.
   - При первом запуске Gradle скачает зависимости из Maven Central.
     Требуется подключение к интернету или локальный кеш артефактов.
   - В проект подключен модуль `spring-boot-starter-validation`,
     поэтому запросы валидируются через Bean Validation.
7. **Запуск тестов**
   - Выполните `./backend/gradlew test`.

8. **Сборка CSS**
   - Выполните `npm install` (или `npm ci`) из корня репозитория.
   - Для обновления стилей Tailwind запустите `npm run build` из корня
     репозитория. Результат появится в `backend/src/main/resources/static`.
   - При деплое workflow GitHub Actions автоматически выполняет эти
     команды, поэтому вручную собирать CSS не требуется.

9. **Прокси**
   - Для получения зависимостей может потребоваться сетевой прокси.
     Укажите его хост и порт в переменных окружения
     `PROXY_HOST` и `PROXY_PORT` (а также `HTTP_PROXY` и `HTTPS_PROXY`
     вида `http://<host>:<port>`), чтобы Maven и Git могли
     подключаться к удалённым репозиториям. Если переменные не заданы,
     прокси не используется.
  - Для автоматического создания настроек воспользуйтесь скриптом
    `scripts/setup-proxy.sh`. Он создаёт `.mvn/settings.xml` только при
    наличии переменных окружения `PROXY_HOST` и `PROXY_PORT`, иначе
    генерирует пустой файл и прокси не используется. Скрипт также
    настраивает git при наличии `HTTP_PROXY`/`HTTPS_PROXY`.
   - Файл `.mvn/jvm.config` по умолчанию пуст, поэтому параметры
     прокси передаются только через переменные окружения.
   - Если проект собирается через GitHub Actions, эти значения
     необходимо задать как секреты репозитория `PROXY_HOST` и
     `PROXY_PORT`.

## Production Deployment

Полный перечень переменных приведён в [docs/ENVIRONMENT.md](docs/ENVIRONMENT.md).

1. При необходимости создайте файл `infra/.env` на основе переменных из
   [docs/ENVIRONMENT.md](docs/ENVIRONMENT.md). Значения для `POSTGRES_*` и других
   переменных имеют безопасные defaults, поэтому файл можно опустить для локального
   запуска. Чтобы использовать собственный секрет, сгенерируйте `JWT_SECRET`,
   например `openssl rand -hex 32`. `docker compose` автоматически считывает
   `infra/.env`, а сам файл исключён из индекса Git.
2. Соберите production артефакты:
   ```bash
   npm --prefix frontend ci
   npm --prefix frontend run build
   ./backend/gradlew clean bootJar
   ```

3. Создайте сертификат для Nginx. Выполните команду из корня репозитория,
   чтобы итоговые файлы оказались в каталоге `infra/nginx/certs`:

   ```bash
   mkdir -p infra/nginx/certs && cd <repo-root>
   openssl req -x509 -newkey rsa:2048 -nodes \
     -keyout infra/nginx/certs/crm-synergy.key \
    -out infra/nginx/certs/crm-synergy.crt \
    -days 365 -subj "/CN=localhost"
  ```
  Чтобы использовать сертификат в CI, закодируйте оба файла в Base64 одной
  строкой (`base64 -w0`) и сохраните полученный текст в секреты `SSL_CERT`,
  `SSL_KEY` и, при наличии цепочки, `SSL_CA_CERT`.
4. Соберите и запустите контейнеры через Makefile. Он использует
   `infra/docker-compose.yml`:

   ```bash
   make build
   make up
   docker compose ps
   curl -f http://localhost:8080/actuator/health
   make down
   ```

Перед повторным запуском остановите предыдущие контейнеры:

```bash
make down
# или
docker compose down --remove-orphans
```

Статус контейнеров можно проверить командой `docker compose ps`.

| Сервис          | Порт контейнера | Порт хоста |
|-----------------|-----------------|------------|
| `db`            | `5432`          | `5432`     |
| `app`           | `8080`          | _не публикуется_ |
| `nginx`         | `80`, `443`     | `8080`, `8443` |
| `nginx-exporter`| `9113`          | `9114`     |
| `prometheus`    | `9090`          | `9090`     |

Порты можно изменить, отредактировав `infra/docker-compose.yml` или передав
флаг `-p` при запуске `docker run`.
Для Nginx предусмотрены переменные `NGINX_HTTP_PORT` и `NGINX_HTTPS_PORT` (по
умолчанию `8080` и `8443`). При необходимости задайте другие значения в файле
`infra/.env` или через переменные окружения перед запуском `docker compose up`.
**Важно:** если на хосте уже работает Nginx или любой процесс, занимающий
:80/:443, отредактируйте файл `.env` и поменяйте `NGINX_HTTP_PORT`,
`NGINX_HTTPS_PORT` (по‑умолчанию 8080 и 8443).
Убедитесь, что порт `8080` свободен: `lsof -i :8080` или `docker ps`.

Если ранее использовалась версия `docker-compose` без явного указания имени проекта,
могли сохраниться контейнеры с префиксом `infra`, занимающие те же порты.
Удалите их вручную:

```bash
docker ps -aq --filter "label=com.docker.compose.project=infra" | xargs -r docker rm -f
```

   Dockerfile собирает JAR внутри образа, поэтому Gradle на хосте не требуется.
   Логи можно смотреть через `make logs`. При необходимости можно запустить
   `docker compose -f infra/docker-compose.yml up -d` напрямую без Makefile.

Контейнер `app` используется как в production, так и при локальной разработке.
`nginx` запускается вместе с приложением и автоматически ждёт готовности бэкенда.

При запуске `nginx` отдельно задайте `APP_HOST`, `APP_PORT` и `SERVER_NAME` для
указания адреса бэкенда и домена, который будет использован в конфигурации
`nginx.conf.template`.
Логи прокси монтируются во внешний каталог `infra/nginx/logs`, чтобы сохраняться между перезапусками контейнера.

После старта веб-интерфейс доступен на `https://localhost` (порт `443`),
обращения к `http://localhost` перенаправляются на HTTPS.

### Kubernetes Deployment
Helm charts for the application reside in `infra/k8s/helm`. Use the
`deploy-k8s.yml` workflow or run `helm upgrade --install` manually.
Before each upgrade a Helm hook launches a short-lived Job to apply
database migrations so the backend starts with the correct schema. The Job
waits for the database via `wait-for-db.sh` and is removed once completed:

```bash
make k8s-deploy
```
The command uses `values.yaml` by default. For a production setup run:
```bash
helm upgrade --install schedule-app infra/k8s/helm/schedule-app \
  --values infra/k8s/helm/schedule-app/values-production.yaml \
  --atomic --wait --timeout 5m
```
The cluster credentials must be provided in `KUBE_CONFIG_B64` and Docker images
are pulled from the registry defined by `DOCKER_REPOSITORY`.
Stateful components request CPU and memory limits via chart values so the
cluster can schedule them with guaranteed resources.

### Проверка сервиса
После деплоя убедитесь, что контейнеры запущены и перешли в состояние `healthy`:
```bash
docker compose ps -a
```
Если сервис `app` отсутствует или имеет статус `Exited`, проверьте лог
следующей командой:
```bash
docker compose logs app
```
Частая причина ошибки 502/503 — приложение не смогло подключиться к базе.
Логи можно просмотреть командой `docker compose logs app` и при необходимости
перезапустить инфраструктуру через `make up`. После проверки завершите работу
командой `make down`.

Проверить здоровье приложения можно запросом к эндпоинту `/actuator/health`:
```bash
curl -f http://localhost:8080/actuator/health
```
Метрики Spring Boot доступны на `/actuator/prometheus` и могут быть
собраны Prometheus:
```bash
curl http://localhost:8080/actuator/prometheus
```


После запуска метрики Nginx доступны на `http://localhost:9114/metrics`.
Экспортер считывает данные со страницы `/nginx_status` внутри контейнера.

Инфраструктура включает сервис `prometheus`, который читает конфигурацию из
файла `infra/prometheus/prometheus.yml` и автоматически опрашивает приложение и
`nginx-exporter`. Запускайте `docker compose -f infra/docker-compose.yml`
из корня проекта, чтобы Docker корректно смонтировал файл. Веб‑интерфейс
Prometheus доступен на `http://localhost:9090`.



### Двухфакторная аутентификация

При успешной регистрации сервер возвращает объект `SignupResponse`,
содержащий поле `secret`. Этот секрет нужно сохранить и добавить в любое
приложение TOTP (Google Authenticator, FreeOTP и т.п.). Сформируйте QR‑код с
помощью утилиты `qrencode`:

```bash
SECRET="JBSWY3DPEHPK3PXP"
qrencode "otpauth://totp/ScheduleTracker:alice?secret=$SECRET&issuer=ScheduleTracker" -o totp.png
```

Если двухфакторная аутентификация не нужна, секрет можно проигнорировать.

Полученный файл `totp.png` можно отсканировать в приложении аутентификации,
либо просто ввести значение `SECRET` вручную. Пример QR‑кода приведён ниже:

![TOTP QR](docs/img/totp-sample.png)

## Веб-интерфейс

SPA реализована на React и располагается в каталоге `frontend/`.
Для локальной разработки выполните в нём:

```bash
cd frontend
npm install
npm run build
```

Скрипт `postbuild` копирует содержимое каталога `dist` в
`backend/src/main/resources/static`, поэтому бэкенд сразу использует
актуальные файлы SPA. Готовый каталог можно раздавать через NGINX или другой web‑сервер.
Из корня репозитория то же самое выполняет команда `make frontend`.

Для локального тестирования предусмотрена команда `npm run dev`, которая
запускает Vite и Tailwind в watch‑режиме и проксирует запросы к бэкенду.


Перед сборкой создайте файл `frontend/.env` и задайте переменную `VITE_API_URL` из [docs/ENVIRONMENT.md](docs/ENVIRONMENT.md).
Файл `.env.example` больше не используется, поэтому `.env` необходимо добавить вручную.
По умолчанию запросы отправляются относительным путём. В production переменная должна быть пустой, так как путь `/api` уже прописан во всех запросах к бэкенду.

### REST API
Основные эндпоинты:
- `POST /api/auth/login` и `POST /api/auth/register`
- CRUD для преподавателей `/api/teachers`
- CRUD для студентов `/api/students`
- CRUD для групп `/api/groups`
- Управление слотами преподавателей `/api/time-slots`
- Шаблоны и слоты доступности `/api/availability`
- Работа с занятиями `/api/lessons` (проверяется наличие слота преподавателя и отсутствие пересечений)
- Получение данных пользователя `/api/users`
- Настройки преподавателя `GET/PUT /api/settings`


## API документация

После запуска приложения документация Swagger доступна по адресу
`/swagger-ui.html`. Там перечислены все доступные REST эндпоинты.

## CI/CD

Репозиторий содержит workflow `.github/workflows/deploy.yml`, который автоматически деплоит приложение на выделенный VPS при пуше в ветку `main`. Для корректной работы необходимо создать следующие секреты репозитория:

- `VPS_HOST` – адрес сервера;
- `VPS_USER` – имя пользователя для подключения;
- `VPS_SSH_KEY` – приватный ключ для подключения;
- `VPS_PASSPHRASE` – парольная фраза для `VPS_SSH_KEY`;
- `VPS_SSH_PORT` – SSH‑порт, если отличается от `22`;
- `PROXY_HOST` и `PROXY_PORT` – при необходимости использовать сетевой прокси.
- `SSL_CERT` и `SSL_KEY` – текст, полученный через `base64 -w0` из файлов сертификата и закрытого ключа для NGINX;
- `SSL_CA_CERT` – содержимое промежуточного сертификата (если используется).

Workflow собирает JAR, автоматически строит SPA и CSS, копирует получившиеся файлы и инфраструктуру на сервер и запускает `docker compose -f infra/docker-compose.yml up -d`.
Сервер должен иметь установленный Docker версии **27.5.1** или новее (API 1.47), так как деплой тестировался на этой версии.
После успешного завершения всех проверок Pull Request в `main` автоматически сливается через auto-merge.

Чтобы воспроизвести проверки CI локально, выполните:

```bash
# один раз настройте git hooks для автoформатирования
git config core.hooksPath scripts/git-hooks

# примените форматирование и запустите тесты
./backend/gradlew spotlessApply test

cd frontend && npm run lint:fix && npm run lint
```

## SMTP

Для отправки писем настройте переменные окружения `SMTP_HOST`, `SMTP_PORT`,
`SMTP_USERNAME`, `SMTP_PASSWORD`, `SMTP_AUTH`, `SMTP_STARTTLS` и `MAIL_FROM`.
По умолчанию проверка почтового сервера отключена свойством
`management.health.mail.enabled=false`, поэтому контейнер остаётся здоровым,
даже если SMTP не настроен.





## Telegram Bot

Для отправки уведомлений в Telegram задайте токен бота в переменной окружения `TELEGRAM_BOT_TOKEN`. Если значение присутствует, приложение использует Telegram Bot API для рассылки сообщений, иначе уведомления только пишутся в лог.

## JWT

Приложение подписывает JWT секретом из переменной окружения `JWT_SECRET`.
В `application.yml` он задан как `${JWT_SECRET:0123456789abcdef0123456789abcdef}`, поэтому
в production его нужно обязательно переопределить и использовать
случайную строку не менее 32 байт,
например `openssl rand -hex 32`. Файл
`infra/docker-compose.yml` требует эту переменную, поэтому запуск
контейнеров завершится ошибкой, если `JWT_SECRET` не задан.

## Backup

Для резервного копирования базы данных выполните скрипт `scripts/backup-db.sh`. Он использует `pg_dump`, а имя файла содержит дату и время создания:

```bash
scripts/backup-db.sh
```

Скрипт читает настройки подключения из переменных `DB_HOST`, `DB_PORT`, `DB_USER`, `DB_PASSWORD` и `DB_NAME`. Готовый архив помещается в каталог `backups` или путь из `BACKUP_DIR`.

Пример запуска по расписанию через cron:

```
0 3 * * * /path/to/repo/scripts/backup-db.sh
```

Under Kubernetes the Helm chart can enable an automated CronJob that runs the
same script nightly. Dumps are stored on a PersistentVolume and rotated
externally.

