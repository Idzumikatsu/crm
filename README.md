# Schedule Tracker

Монорепозиторий включает backend на Spring Boot и SPA на React.

## Структура проекта

- `backend/` — серверная часть и REST API
- `frontend/` — клиентское приложение Vite/React
- `infra/` — Docker Compose и конфигурация NGINX
- `docs/` — документация и ADR. Также в каталоге находится файл
  `ARCHITECTURE_IMPROVEMENTS.md` с планом дальнейшего развития
  инфраструктуры и рекомендациями по запуску

## Настройка окружения

1. **Java 21**
   - Установите JDK 21. На Linux можно установить пакет `openjdk-21-jdk`.
2. **Gradle**
   - Используйте Gradle 8.14 или запускайте через прилагаемый скрипт `./backend/gradlew`.
3. **Node.js**
   - Use Node 20 or 22 as in the CI matrix.
   - Run `npm install` (or `npm ci`) before any npm script (`npm run build`, `npm run lint`, `npm run dev`).
   - Проверить стиль кода можно командой `cd frontend && npm run lint`.
   - Большинство ошибок форматирования исправляется автоматически
     через `cd frontend && npm run lint:fix`.
4. **PostgreSQL**
   - Приложение ожидает базу `schedule` с пользователем `postgres` и паролем `postgres`.
   - Быстрый вариант через Docker:
     ```bash
     docker run --name schedule-db -p 5432:5432 -e POSTGRES_DB=schedule \
      -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -d postgres:16.2
     ```
    - В `application.yml` адрес БД задаётся через переменную окружения `DB_HOST`.
      По умолчанию используется `db` (можно переопределить). Если профиль
      `postgres` не активирован, используется встроенная база H2.
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
   - При деплое workflow GitHub Actions автоматически выполняет эти
     команды, поэтому вручную собирать CSS не требуется.

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

1. Создайте файл `infra/.env`, скопировав `infra/.env.example`, и замените
   значение `JWT_SECRET` (по умолчанию `0123456789abcdef0123456789abcdef`) случайной строкой
   длиной не менее 32 байт, например `openssl rand -hex 32`.
   При необходимости измените `SPRING_PROFILES_ACTIVE`, `DB_HOST` и `DB_PORT`.
   Переменная `DB_HOST` по умолчанию равна `db`, но её можно переопределить.
   Файл автоматически читается `docker compose` из `infra/.env`. Если переменная
   не задана, `docker compose` завершится ошибкой `JWT_SECRET: set JWT_SECRET in .env`.
   Файл добавлен в `.gitignore` и хранится локально.
2. Создайте сертификат для Nginx. Выполните команду из корня репозитория,
   чтобы итоговые файлы оказались в каталоге `infra/nginx/certs`:

   ```bash
   mkdir -p infra/nginx/certs && cd <repo-root>
   openssl req -x509 -newkey rsa:2048 -nodes \
     -keyout infra/nginx/certs/server.key \
     -out infra/nginx/certs/server.crt \
     -days 365 -subj "/CN=localhost"
   ```
3. Соберите и запустите контейнеры через Makefile. Он использует
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
| `app`           | `8080`          | `8080`     |
| `nginx`         | `80`, `443`     | `80`, `443`|
| `nginx-exporter`| `9113`          | `9114`     |
| `prometheus`    | `9090`          | `9090`     |

Порты можно изменить, отредактировав `infra/docker-compose.yml` или передав
флаг `-p` при запуске `docker run`.
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

При запуске `nginx` отдельно задайте `APP_HOST` и `APP_PORT` для указания адреса
бэкенда. Контейнер подставит их в `nginx.conf.template`.

После старта веб-интерфейс доступен на `https://localhost` (порт `443`),
обращения к `http://localhost` перенаправляются на HTTPS.

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
появятся в `frontend/dist`. Скрипт `postbuild` автоматически копирует их в
`backend/src/main/resources/static`, поэтому бэкенд сразу использует свежие файлы
SPA. Готовый каталог можно раздавать через NGINX или другой web-сервер.
Из корня репозитория то же самое выполняет команда `make frontend`.

Для совместимости в каталоге `backend/src/main/resources/static` остаются
упрощённые HTML-страницы, доступные без авторизации.

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
- `VPS_PASSWORD` – пароль пользователя;
- `VPS_PORT` – SSH‑порт, если отличается от `22`;
- `PROXY_HOST` и `PROXY_PORT` – при необходимости использовать сетевой прокси.

Workflow собирает JAR, автоматически строит SPA и CSS, копирует получившиеся файлы и инфраструктуру на сервер и запускает `docker compose -f infra/docker-compose.yml up -d`.
Сервер должен иметь установленный Docker версии **27.5.1** или новее (API 1.47), так как деплой тестировался на этой версии.
После успешного завершения всех проверок Pull Request в `main` автоматически сливается через auto-merge.

Чтобы воспроизвести проверки CI локально, выполните:

```bash
./backend/gradlew test
cd frontend && npm run lint:fix && npm run lint
```





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

