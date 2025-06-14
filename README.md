# Schedule Tracker

Монорепозиторий включает backend на Spring Boot и SPA на React.

## Структура проекта

- `backend/` — серверная часть и REST API
- `frontend/` — клиентское приложение Vite/React
- `infra/` — системные файлы для деплоя через systemd
- `docs/` — документация и ADR. Также в каталоге находится файл
  `ARCHITECTURE_IMPROVEMENTS.md` с планом дальнейшего развития
  инфраструктуры и рекомендациями по запуску

## Настройка окружения //

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
   - В `application.yml` адрес БД задаётся через переменную окружения `DB_HOST`.
     По умолчанию используется `db` (можно переопределить). Основной профиль
     `postgres` рассчитан на PostgreSQL. Для локальной разработки оставлен профиль `dev`, который использует встроенную базу H2.
   - Для миграций используется Flyway **11.9.1**, что обеспечивает поддержку
     PostgreSQL 16.2.
   - Вся конфигурация хранится в `backend/src/main/resources/application.yml`
     и профильных YAML‑файлах. Для сложных роутов в `SpaController` в этом
     файле задана настройка `spring.mvc.pathmatch.matching-strategy=ant_path_matcher`.
6. **Сборка и запуск backend**
   - Сборка: `./backend/gradlew build`
   - Запуск приложения: `./backend/gradlew bootRun`
   - Перед запуском убедитесь, что переменная `SPRING_PROFILES_ACTIVE` установлена в `postgres`.
   - Приложение слушает порт `8080`. Убедитесь, что этот порт свободен
     перед запуском, иначе старт завершится ошибкой.
   - При первом запуске Gradle скачает зависимости из Maven Central.
     Требуется подключение к интернету или локальный кеш артефактов.
   - В проект подключен модуль `spring-boot-starter-validation`,
     поэтому запросы валидируются через Bean Validation.
7. **Запуск тестов**
   - Выполните `./backend/gradlew test`.

8. **Прокси**
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

## Развёртывание на сервере
> **Примечание.** Разработка ведётся напрямую на VPS `crm-synergy.ru` без
> тестовых окружений. Любой push в `main` запускает workflow
> [`deploy-vps.yml`](.github/workflows/deploy-vps.yml) и сразу деплоит
> изменения в продакшн, поэтому перед пушем необходимо тщательно проверить
> код локально.

Краткая последовательность команд для ручного обновления приложения:

```bash
npm --prefix frontend ci
npm --prefix frontend run build
./backend/gradlew bootJar

scp backend/build/libs/*.jar $VPS_USER@$VPS_HOST:/opt/schedule-app/app.jar
# при первом запуске копируйте systemd‑юнит
scp infra/systemd/schedule-app.service \
  $VPS_USER@$VPS_HOST:/etc/systemd/system/schedule-app.service
ssh $VPS_USER@$VPS_HOST 'sudo systemctl daemon-reload && sudo systemctl enable schedule-app'
ssh $VPS_USER@$VPS_HOST 'sudo systemctl restart schedule-app'
```

Файл с переменными окружения должен находиться на сервере по пути `/etc/schedule-app.env`.

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
Команда `npm --prefix frontend run build` из корня репозитория собирает SPA и генерирует все необходимые стили.

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

Репозиторий содержит workflow `.github/workflows/deploy-vps.yml`, который автоматически деплоит приложение на выделенный VPS при пуше в ветку `main`. Для корректной работы необходимо создать следующие секреты репозитория:

- `VPS_HOST` – адрес сервера;
- `VPS_USER` – имя пользователя для подключения;
- `VPS_SSH_KEY` – приватный ключ для подключения;
- `VPS_PASSPHRASE` – парольная фраза для `VPS_SSH_KEY`;
- `VPS_SSH_PORT` – SSH‑порт, если отличается от `22`;
- `PROXY_HOST` и `PROXY_PORT` – при необходимости использовать сетевой прокси.

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
`management.health.mail.enabled=false`, поэтому приложение остаётся работоспособным,
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


