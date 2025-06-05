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


