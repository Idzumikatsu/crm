# Schedule Tracker

Монорепозиторий включает backend на Spring Boot и SPA на React.

## Структура проекта

- `backend/` — серверная часть и REST API
- `frontend/` — клиентское приложение Vite/React
- `infra/` — Helm charts и конфигурация NGINX
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
Документация по развёртыванию PostgreSQL через Terraform
находится в [docs/TERRAFORM_DB.md](docs/TERRAFORM_DB.md).

1. При необходимости создайте файл `infra/.env` на основе переменных из
   [docs/ENVIRONMENT.md](docs/ENVIRONMENT.md). Значения для `POSTGRES_*` и других
   переменных имеют безопасные defaults, поэтому файл можно опустить для локального
   запуска. Чтобы использовать собственный секрет, сгенерируйте `JWT_SECRET`,
   например `openssl rand -hex 32`. При необходимости можно запустить
   RabbitMQ, указав `RABBITMQ_USER` и `RABBITMQ_PASSWORD` в `infra/.env`.
2. Соберите production артефакты:
   ```bash
   npm --prefix frontend ci
   npm --prefix frontend run build
   ./backend/gradlew clean bootJar
   ```

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
are pulled from GitHub Container Registry.
Stateful components request CPU and memory limits via chart values so the
cluster can schedule them with guaranteed resources.
Each pod runs under its own ServiceAccount with tokens disabled by default
to follow the least-privilege principle.

### Проверка сервиса
После деплоя убедитесь, что все поды находятся в состоянии `Running` и
готовы принимать трафик:
```bash
kubectl get pods -n schedule
```
Если приложение не стартует, изучите журналы выбранного пода:
```bash
kubectl logs -n schedule deployment/schedule-app-backend
```
Частая причина ошибки 502/503 — приложение не смогло подключиться к базе.
При необходимости выполните `helm rollback schedule-app <revision>` или
переустановите релиз командой `helm upgrade --install`.

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
из корня проекта, чтобы Docker корректно смонтировал файлы. Веб‑интерфейс
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
например `openssl rand -hex 32`. Значение должно храниться в секрете и
передаваться как переменная окружения `JWT_SECRET` при запуске приложения.

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
 //

Under Kubernetes the Helm chart can enable an automated CronJob that runs the
same script nightly. Dumps are stored on a PersistentVolume and rotated
externally.

