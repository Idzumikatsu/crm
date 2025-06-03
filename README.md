# Schedule Tracker

## Настройка окружения

1. **Java 17**
   - Установите JDK 17. На Linux можно установить пакет `openjdk-17-jdk`.
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

## Учетные записи

После первого запуска создается учетная запись администратора.
Параметры входа:
- логин: `admin`
- пароль: `admin`


