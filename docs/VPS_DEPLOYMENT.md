# Развертывание на VPS

Этот документ описывает ручное развёртывание приложения на сервер `crm-cynergy.ru` без Docker и Kubernetes.

## Сборка

```bash
npm --prefix frontend ci
npm --prefix frontend run build
./backend/gradlew clean bootJar
```

После выполнения команд исполняемый JAR находится в `backend/build/libs`, а статика React скопирована в `backend/src/main/resources/static`.

## Доставка на сервер

Передайте JAR и конфигурацию на VPS, например с помощью `scp`:

```bash
scp backend/build/libs/*.jar $VPS_USER@$VPS_HOST:/opt/schedule-app/app.jar
```

При первом развёртывании также установите systemd‑юнит:

```bash
scp infra/systemd/schedule-app.service \
  $VPS_USER@$VPS_HOST:/etc/systemd/system/schedule-app.service
ssh $VPS_USER@$VPS_HOST 'sudo systemctl daemon-reload && sudo systemctl enable schedule-app'
```

## Перезапуск сервиса

На сервере приложение запускается как systemd‑служба `schedule-app`. После обновления файла нужно перезапустить сервис:

```bash
ssh $VPS_USER@$VPS_HOST 'sudo systemctl restart schedule-app'
```

Переменные окружения хранятся в `/etc/schedule-app.env`. Секреты следует задавать там и ограничивать доступ к файлу.
