# Schedule Tracker

## Настройка окружения

1. **Java 21**
   - Установите JDK 21. На Linux можно установить пакет `openjdk-21-jdk`.
2. **Maven**
   - Используйте Maven 3.9.9 или запускайте через прилагаемый скрипт `./mvnw`.
3. **PostgreSQL**
   - Приложение ожидает базу `schedule` с пользователем `postgres` и паролем `postgres`.
   - Быстрый вариант через Docker:
     ```bash
     docker run --name schedule-db -p 5432:5432 -e POSTGRES_DB=schedule \
       -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -d postgres:15
     ```
   - В `application.yml` адрес БД задаётся через переменную окружения `DB_HOST`.
     По умолчанию используется `localhost`.
4. **Сборка и запуск**
   - Сборка: `./mvnw package`
   - Запуск: `./mvnw spring-boot:run`
5. **Запуск тестов**
   - Выполните `./mvnw test`.

6. **Прокси**
   - Для получения зависимостей требуется сетевой прокси.
     Укажите его хост и порт в переменных окружения
     `PROXY_HOST` и `PROXY_PORT` (а также `HTTP_PROXY` и `HTTPS_PROXY`
     вида `http://<host>:<port>`), чтобы Maven и Git могли
     подключаться к удалённым репозиториям.
   - Скопируйте файл `.mvn/settings.xml.in` в `.mvn/settings.xml`,
     подставив значения этих переменных. Проще всего сделать это
     командой `envsubst < .mvn/settings.xml.in > .mvn/settings.xml`.
   - Если проект собирается через GitHub Actions, эти значения
     необходимо задать как секреты репозитория `PROXY_HOST` и
     `PROXY_PORT`.

## Запуск в Docker

Приложение использует профиль `postgres`, поэтому его нужно активировать
через переменную `SPRING_PROFILES_ACTIVE`. Также контейнеру требуется адрес
сервиса PostgreSQL в переменной `DB_HOST`. Если вы создавали контейнер БД по
примеру выше, его имя `schedule-db`. Для связи контейнеров понадобится сеть
`app-network`; создайте её при необходимости командой `docker network create
app-network`. Запуск приложения выглядит так:

```bash
docker run --rm -it --network app-network \
  -e DB_HOST=schedule-db \
  -e SPRING_PROFILES_ACTIVE=postgres \
  --name my-java-app my-java-app
```


## Учетные записи

После первого запуска создаются учетные записи менеджера и преподавателя.
Параметры входа:
- менеджер: `manager`/`manager`
- преподаватель: `teacher`/`teacher`



## Node/React CRM prototype

### Running tests

```bash
cd server && npm test
cd ../client && npm test
```

### Starting applications

**Server**

```bash
cd server
JWT_SECRET=devsecret npm start
```

**Client**

```bash
cd client
npm start
```

Set environment variables `JWT_SECRET` and optional `DATABASE_URL` for the server.
