# Project Task Breakdown

## Milestone 1: Project Setup & Authentication

### 1.1 Инициализация проекта
- [x] Создать Maven-проект и указать `groupId`/`artifactId`.
- [x] Подключить зависимости: web, security, data-jpa, jjwt, драйвер БД, тестовые библиотеки.
- [x] Добавить `application.yml` с настройками БД, JPA и JWT.
- [x] Убедиться, что приложение стартует.

### 1.2 JWT-аутентификация
- [x] Класс `JwtUtils` для генерации и проверки токенов.
- [x] Фильтр `JwtFilter`.
- [x] Конфигурация безопасности с подключением фильтра и правилами доступа.
- [x] `UserDetailsServiceImpl`.
- [x] Сущности `User` и `Role`, репозиторий `UserRepository`.
- [x] `AuthController` с эндпоинтами `/login` и `/register`.
- [ ] DTO: `LoginRequest`, `SignupRequest`, `JwtResponse` (пока используются `Map`).
- [x] Бин `PasswordEncoder`.
- [x] Тесты `AuthControllerTest`.

### 1.3 Сущности и миграции
- [x] Сущности `Teacher` и `Student`.
- [x] Таблица-связка `TeacherStudent`.
- [ ] Настроить связи `@ManyToOne` и коллекции в `Teacher`/`Student`.
- [ ] SQL‑миграции через Flyway/Liquibase.
- [ ] Интеграционный тест схемы.

## Milestone 5: Веб-интерфейс (Thymeleaf + Tailwind CSS + Alpine.js)

### 5.1 Базовая структура
- [ ] Настроить Tailwind (`tailwind.config.js`, сборка `styles.css`).
- [ ] Создать Thymeleaf-фрагменты `head`, `navbar`, `footer`.
- [ ] Общий шаблон `layout.html`.

### 5.2 Страница входа
- [x] Шаблон `login.html`.
- [x] Контроллер `WebController.login()`.
- [ ] Редиректы после входа в зависимости от роли.

### 5.3 Навигация и профиль
- [ ] Фрагмент `navbar` с учётом роли пользователя.
- [ ] Страница `profile.html`.

### 5.4 Manager Dashboard
- [ ] REST: `GET /api/manager/teachers` и `/api/manager/students`.
- [ ] UI для назначения студентов преподавателям (modal, Alpine.js).
- [ ] `POST /api/manager/assign` для обновления связей.
- [ ] Отображение расписания через FullCalendar.

### 5.5 Teacher Dashboard
- [ ] REST для получения и изменения собственных уроков.
- [ ] Календарь занятий через FullCalendar.
- [ ] Форма создания и редактирования урока.

## Milestone 7: Manager Dashboard — Назначение студентов
- Задачи совпадают с пунктами 5.4 и пока не выполнены.

## Milestone 8: Testing & Deployment

### 8.1 Backend
- [x] Подключён `jacoco-maven-plugin`.
- [ ] Дописать unit‑тесты сервисов.
- [ ] Интеграционные тесты контроллеров.
- [ ] Проверить отчёт Jacoco и довести покрытие до 80 %.

### 8.2 UI Integration Tests
- [ ] Настроить Selenium/Cypress.
- [ ] Тест логина.
- [ ] Тесты доступа по ролям.
- [ ] Тест менеджерской панели.
- [ ] Тест страницы преподавателя.
- [ ] Запуск UI‑тестов в CI.
