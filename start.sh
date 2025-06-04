#!/usr/bin/env bash
# Скрипт запуска приложения в зависимости от типа проекта
set -e

if [ -f pom.xml ]; then
  echo "Запускается Spring Boot приложение"
  exec ./mvnw spring-boot:run
elif [ -f package.json ]; then
  echo "Запускается Node.js приложение"
  exec npm start
elif [ -f requirements.txt ]; then
  echo "Запускается Python приложение"
  if [ -f app.py ]; then
    exec python3 app.py
  elif [ -f main.py ]; then
    exec python3 main.py
  else
    echo "Не найден app.py или main.py" >&2
    exit 1
  fi
elif [ -x myapp ]; then
  echo "Запускается Go бинарник"
  exec ./myapp
else
  echo "Не удалось определить тип приложения" >&2
  exit 1
fi
