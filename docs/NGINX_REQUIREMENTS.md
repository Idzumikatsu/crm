# NGINX Requirements

## 1. Сбор исходных данных

### 1.1 Ответственный
За аудит отвечает **Ivan Petrov**, SRE Lead.

### 1.2–1.3 Перечень HTTP эндпоинтов
Все текущие сервисы используют HTTP/1.1. Ни WebSocket, ни gRPC не применяются.

| Метод | URI | Протокол |
|-------|-----|----------|
| POST | /api/auth/login | HTTP/1.1 |
| POST | /api/auth/register | HTTP/1.1 |
| GET | /api/groups | HTTP/1.1 |
| GET | /api/groups/{id} | HTTP/1.1 |
| POST | /api/groups | HTTP/1.1 |
| PUT | /api/groups/{id} | HTTP/1.1 |
| DELETE | /api/groups/{id} | HTTP/1.1 |
| GET | /api/hello | HTTP/1.1 |
| GET | /api/lessons | HTTP/1.1 |
| GET | /api/lessons/{id} | HTTP/1.1 |
| POST | /api/lessons | HTTP/1.1 |
| PUT | /api/lessons/{id} | HTTP/1.1 |
| DELETE | /api/lessons/{id} | HTTP/1.1 |
| PATCH | /api/lessons/{id}/status | HTTP/1.1 |
| GET | /api/manager/teachers | HTTP/1.1 |
| GET | /api/manager/students | HTTP/1.1 |
| POST | /api/manager/assign | HTTP/1.1 |
| GET | /api/students | HTTP/1.1 |
| GET | /api/students/{id} | HTTP/1.1 |
| POST | /api/students | HTTP/1.1 |
| PUT | /api/students/{id} | HTTP/1.1 |
| DELETE | /api/students/{id} | HTTP/1.1 |
| GET | /api/teachers | HTTP/1.1 |
| GET | /api/teachers/{id} | HTTP/1.1 |
| POST | /api/teachers | HTTP/1.1 |
| PUT | /api/teachers/{id} | HTTP/1.1 |
| DELETE | /api/teachers/{id} | HTTP/1.1 |
| GET | /api/teacher/lessons | HTTP/1.1 |
| PUT | /api/teacher/lessons/{id} | HTTP/1.1 |
| GET | /api/time-slots | HTTP/1.1 |
| GET | /api/time-slots/{id} | HTTP/1.1 |
| POST | /api/time-slots | HTTP/1.1 |
| PUT | /api/time-slots/{id} | HTTP/1.1 |
| DELETE | /api/time-slots/{id} | HTTP/1.1 |
| GET | /api/users/me | HTTP/1.1 |
| GET | /api/users/{username} | HTTP/1.1 |
| POST | /api/users | HTTP/1.1 |


### 1.4 Метрики нагрузки
Средняя нагрузка приложения оценивается в **50 rps** при пиковых значениях до
**200 rps**. В течение последнего месяца наблюдается стабильный рост около
**10% в неделю**.

### 1.5 Требуемые SLA
- p95 задержки ответа: не более **200&nbsp;мс**;
- p99 задержки ответа: не более **500&nbsp;мс**;
- целевая доступность: **99.9%**.

### 1.6 Требования к защите данных
- логирование без персональных данных (соблюдение GDPR);
- хранение логов не более 30 дней с ограничением доступа;
- всё сетевое взаимодействие должно быть защищено TLS.

### 1.7 Завершение TLS
В текущей топологии TLS не используется. Планируется завершать его на уровне
NGINX.

### 1.8 Распределение обязанностей
- NGINX отвечает за SSL-терминацию, кеш статических ресурсов и ограничения
  скорости;
- приложение реализует аутентификацию, авторизацию и бизнес‑логику;
- правила WAF настраиваются в NGINX.

### 1.9 Консолидация
Настоящий документ фиксирует все выводы этапа сбора данных и используется в
дальнейших шагах по настройке NGINX.


## 2. Проектная топология

### 2.1 Схема сетевых потоков
Трафик от пользователей поступает на контейнер NGINX, который проксирует
его к приложению Spring Boot. Приложение обращается к базе PostgreSQL.
Метрики NGINX экспортируются через отдельный контейнер.

### 2.2 Режим развёртывания
NGINX запускается как отдельный сервис внутри Docker Compose, выполняя роль
общего входного шлюза.

### 2.3 Балансировка трафика
При появлении нескольких экземпляров приложения балансировка будет
осуществляться стандартным алгоритмом `round_robin`.

### 2.4 HTTP/2 и HTTP/3
Клиентские приложения поддерживают HTTP/2. Его подключение планируется на
внешнем интерфейсе NGINX. Переход на HTTP/3 пока не требуется.

### 2.5 Политика релизов
Используется стратегия blue‑green для безрисковых обновлений конфигурации
и приложения.

### 2.6 Целевая архитектура
Принята схема, описанная в `NGINX_DESIGN.md`, которая фиксирует все вышеуказанные
решения.
