FROM eclipse-temurin:21-jre
WORKDIR /app
COPY scheduletracker-0.0.1-SNAPSHOT.jar app.jar
# По умолчанию приложение ожидает базу данных на `localhost`, что не
# подходит для контейнера. Добавим переменную окружения с адресом
# хоста БД. Значение можно переопределить при запуске.
ENV DB_HOST=host.docker.internal
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
