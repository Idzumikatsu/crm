# NGINX Deployment Process

Этот документ описывает доставку конфигурации NGINX и перезагрузку сервиса на VPS.

## Repository and Reviews
Configuration lives in the `nginx/` directory of the main repository. All changes go through pull requests and require at least one approval before merge.

## Проверка конфигурации
Конфигурационные файлы расположены в каталоге `infra/nginx`. Перед отправкой на сервер выполните `nginx -t -c nginx.conf` локально, чтобы убедиться в отсутствии синтаксических ошибок.

## Стратегия развёртывания
После проверки конфигурация копируется на сервер и выполняется перезагрузка NGINX. При ошибке сервер остаётся с прежними настройками.

## Шаги доставки
1. После сборки приложения скопируйте `nginx.conf` и JAR на сервер:
   ```bash
   scp infra/nginx/nginx.conf $VPS_USER@$VPS_HOST:/etc/nginx/nginx.conf
   scp backend/build/libs/*.jar $VPS_USER@$VPS_HOST:/opt/schedule-app/app.jar
   scp infra/systemd/schedule-app.service \
       $VPS_USER@$VPS_HOST:/etc/systemd/system/schedule-app.service
   ```
2. Подключитесь по SSH и перезапустите сервисы:
   ```bash
   ssh $VPS_USER@$VPS_HOST '\
     sudo systemctl daemon-reload && \
     sudo systemctl restart nginx schedule-app'
   ```
