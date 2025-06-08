# Schedule Tracker

Монорепозиторий включает backend на Spring Boot и SPA на React.

## Структура проекта

- `backend/` — серверная часть и REST API
- `frontend/` — клиентское приложение Vite/React
- `infra/` — Docker Compose и конфигурация NGINX
- `docs/` — документация и ADR

## Настройка окружения

1. **Java 21**
   - Установите JDK 21. На Linux можно установить пакет `openjdk-21-jdk`.
2. **Gradle**
   - Используйте Gradle 8.14 или запускайте через прилагаемый скрипт `./backend/gradlew`.
3. **Node.js**
   - Use Node 20 or 22 as in the CI matrix.
   - Run `npm install` (or `npm ci`) before any npm script (`npm run build`, `npm run lint`, `npm run dev`).
   - Проверить стиль кода можно командой `cd frontend && npm run lint`.
4. **PostgreSQL**
   - Приложение ожидает базу `schedule` с пользователем `postgres` и паролем `postgres`.
   - Быстрый вариант через Docker:
     ```bash
     docker run --name schedule-db -p 5432:5432 -e POSTGRES_DB=schedule \
      -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -d postgres:16.2
     ```
    - В `application.yml` адрес БД задаётся через переменную окружения `DB_HOST`.
      По умолчанию используется `localhost`. Если профиль `postgres` не активирован,
      используется встроенная база H2.
    - Для миграций используется Flyway **11.9.1**, что обеспечивает поддержку
      PostgreSQL 16.2.
5. **Сборка и запуск backend**
   - Сборка: `./backend/gradlew build`
   - Запуск: `./backend/gradlew bootRun`
   - Приложение слушает порт `8080`. Убедитесь, что этот порт свободен
     перед запуском, иначе старт завершится ошибкой.
   - При первом запуске Gradle скачает зависимости из Maven Central.
     Требуется подключение к интернету или локальный кеш артефактов.
   - В проект подключен модуль `spring-boot-starter-validation`,
     поэтому запросы валидируются через Bean Validation.
6. **Запуск тестов**
   - Выполните `./backend/gradlew test`.

7. **Сборка CSS**
   - Выполните `npm install` (или `npm ci`) из корня репозитория.
   - Для обновления стилей Tailwind запустите `npm run build` из корня
     репозитория. Результат появится в `backend/src/main/resources/static`.

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

## Запуск в Docker

Перед первым запуском скопируйте `.env.example` в `.env` и задайте значения
`SPRING_PROFILES_ACTIVE`, `DB_HOST`, `DB_PORT` и `JWT_SECRET`. `docker compose`
автоматически подхватит этот файл. Приложение использует профиль `postgres`,
поэтому переменные по умолчанию подходят для локального запуска. Если вы
создавали контейнер БД по примеру выше, его имя `schedule-db`. Для связи
контейнеров понадобится сеть `app-network`; создайте её при необходимости
командой `docker network create app-network`. Запуск приложения выглядит так:

```bash
docker run --rm -it --network app-network \
  -e DB_HOST=schedule-db \
  -e SPRING_PROFILES_ACTIVE=postgres \
  --name my-java-app my-java-app
```

Для удобства можно воспользоваться `infra/docker-compose.dev.yml`, который
поднимет PostgreSQL, само приложение и Nginx в роли обратного прокси. Перед
запуском убедитесь, что в каталоге `backend/` есть `app.jar`. Также
создайте самоподписанный сертификат в `infra/nginx/certs`:

```bash
openssl req -x509 -newkey rsa:2048 -nodes -keyout infra/nginx/certs/server.key -out infra/nginx/certs/server.crt -days 365 -subj "/CN=localhost"
```

```bash
./gradlew build
cp $(ls build/libs/*.jar | grep -v plain | head -n 1) app.jar
cd ..
docker compose -f infra/docker-compose.dev.yml up --build
```
Секрет `JWT_SECRET` можно сгенерировать командой `openssl rand -hex 32` и
записать его в `.env`. Не используйте значение `changeme` в production.

При запуске `nginx` отдельно используйте переменные окружения `APP_HOST` и
`APP_PORT` для указания адреса backend-сервиса. Контейнер автоматически
подставит их в `nginx.conf.template`.

После старта контейнеров веб-интерфейс доступен по адресу
`https://localhost` (порт `443`). Обращения к `http://localhost` будут
автоматически перенаправлены на HTTPS.

### Проверка сервиса
После деплоя убедитесь, что контейнеры запущены:
```bash
docker compose ps -a
```
Если сервис `app` отсутствует или имеет статус `Exited`, убедитесь,
что перед сборкой был создан `app.jar`, и просмотрите лог:
```bash
docker compose logs app
```
Частая причина ошибки 502/503 — приложение не смогло подключиться к базе. Запустите контейнеры повторно:
```bash
docker compose -f infra/docker-compose.dev.yml up -d
```

После запуска метрики Nginx доступны на `http://localhost:9114/metrics`.
Экспортер считывает данные со страницы `/nginx_status` внутри контейнера.



## Учетные записи

После первого запуска создаются учетные записи менеджера, преподавателя и администратора.
Параметры входа:
- менеджер: `manager`/`manager`
- преподаватель: `teacher`/`teacher`
- администратор: `admin`/`admin`

При активном профиле `test` эти пользователи не создаются, что упрощает
написание интеграционных тестов.

Новые пользователи могут зарегистрироваться через POST `/api/auth/register`,
передав `username`, `password` и опциональное `role` в теле запроса.

### Двухфакторная аутентификация

При успешной регистрации сервер возвращает объект `SignupResponse`,
содержащий поле `secret`. Этот секрет нужно сохранить и добавить в любое
приложение TOTP (Google Authenticator, FreeOTP и т.п.). Сформируйте QR‑код с
помощью утилиты `qrencode`:

```bash
SECRET="JBSWY3DPEHPK3PXP"
qrencode "otpauth://totp/ScheduleTracker:alice?secret=$SECRET&issuer=ScheduleTracker" -o totp.png
```

Полученный файл `totp.png` можно отсканировать в приложении аутентификации,
либо просто ввести значение `SECRET` вручную. Пример QR‑кода приведён ниже:

![TOTP QR](docs/img/totp-sample.png)

## Веб-интерфейс

SPA реализована на React и располагается в каталоге `frontend/`.
Для локальной разработки выполните в нём:

```bash
cd frontend
npm install
npm run dev
```

Приложение будет доступно на `http://localhost:5173` и использует REST API
бэкэнда на порту 8080.

Производственная сборка выполняется командой `npm run build`, после чего файлы
появятся в `frontend/dist`. Их можно раздавать через NGINX или другой web-сервер.

Для совместимости в каталоге `backend/src/main/resources/static` остаются
упрощённые HTML-страницы, доступные без авторизации.

### REST API
Основные эндпоинты:
- `POST /api/auth/login` и `POST /api/auth/register`
- CRUD для преподавателей `/api/teachers`
- CRUD для студентов `/api/students`
- CRUD для групп `/api/groups`
- Управление слотами преподавателей `/api/time-slots`
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
- `VPS_PASSWORD` – пароль пользователя;
- `VPS_PORT` – SSH‑порт, если отличается от `22`;
- `PROXY_HOST` и `PROXY_PORT` – при необходимости использовать сетевой прокси.

Workflow собирает JAR, копирует его и файлы инфраструктуры на сервер и запускает `docker compose -f infra/docker-compose.dev.yml up -d`.
Сервер должен иметь установленный Docker версии **27.5.1** или новее (API 1.47), так как деплой тестировался на этой версии.
После успешного завершения всех проверок Pull Request в `main` автоматически сливается через auto-merge.

Чтобы воспроизвести проверки CI локально, выполните:

```bash
./backend/gradlew test
cd frontend && npm run lint
```





## Telegram Bot

Для отправки уведомлений в Telegram задайте токен бота в переменной окружения `TELEGRAM_BOT_TOKEN`. Если значение присутствует, приложение использует Telegram Bot API для рассылки сообщений, иначе уведомления только пишутся в лог.

## JWT

Приложение подписывает JWT секретом из переменной окружения `JWT_SECRET`. По умолчанию используется значение `changeme`, поэтому для production окружений обязательно задайте собственный ключ.

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

