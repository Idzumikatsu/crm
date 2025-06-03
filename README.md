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

## Запуск в Docker

Чтобы контейнер приложения смог подключиться к базе данных, при запуске нужно
передать адрес сервиса PostgreSQL через переменную окружения `DB_HOST`. Если вы
создавали контейнер БД по примеру выше, его имя `schedule-db`, поэтому команда
будет такой:

```bash
docker run --rm -it --network app-network \
  -e DB_HOST=schedule-db --name my-java-app my-java-app
```


## Учетные записи

После первого запуска создается учетная запись администратора.
Параметры входа:
- логин: `admin`
- пароль: `admin`


