# Используем официальный образ OpenJDK
FROM openjdk:17-jdk-slim

# Копируем jar файл в контейнер
COPY target/demo-0.0.1-SNAPSHOT.jar /usr/app/demo.jar

# Устанавливаем рабочую директорию
WORKDIR /usr/app

# Устанавливаем команду для запуска приложения
ENTRYPOINT ["java", "-jar", "demo.jar"]