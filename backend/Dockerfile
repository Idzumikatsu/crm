# ---- Build stage -----------------------------------------------------------
FROM gradle:8-jdk21 AS build
WORKDIR /workspace
COPY backend /workspace/backend
WORKDIR /workspace/backend
# Собираем исполняемый JAR без запуска тестов
RUN ./gradlew bootJar --no-daemon
RUN cp $(ls build/libs/*.jar | grep -v plain | head -n 1) app.jar

# ---- Runtime stage ---------------------------------------------------------
FROM eclipse-temurin:21-jre
RUN apt-get update \
    && apt-get install -y --no-install-recommends postgresql-client \
    && rm -rf /var/lib/apt/lists/*
WORKDIR /app
COPY --from=build /workspace/backend/app.jar app.jar
# Скрипт ожидания доступности базы данных
COPY scripts/wait-for-db.sh /wait-for-db.sh
RUN chmod +x /wait-for-db.sh
# По умолчанию приложение ожидает базу данных на `localhost`, что не
# подходит для контейнера. Добавим переменную окружения с адресом
# хоста БД. Значение можно переопределить при запуске.
ENV DB_HOST=host.docker.internal
EXPOSE 8080
ENTRYPOINT ["/wait-for-db.sh","java","-jar","/app/app.jar"]
