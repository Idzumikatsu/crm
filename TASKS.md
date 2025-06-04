# Project Task Breakdown

This file lists detailed sub-tasks for implementing the CRM application.

## Milestone 1: Project Setup & Authentication
- **Setup Spring Boot project**  
  Files: `pom.xml`, `src/**`  
  Effort: 1 day
- **Configure JWT authentication**  
  Add `JwtUtils` helper, `JwtFilter`, update `SecurityConfig`.
  Create `AuthController` с `POST /api/auth/login` и `POST /api/auth/register`.
  Files: `src/main/java/com/example/scheduletracker/config/*`, `src/main/java/com/example/scheduletracker/controller/AuthController.java`.  
  Tests: `AuthControllerTest`.  
  Effort: 2 days
- **Initial entities and migrations**  
  Create `TeacherStudent` join entity.  
  Ensure `User`, `Teacher`, `Student` tables exist via JPA/Hibernate ddl-auto or migrations.  
  Files: `src/main/java/com/example/scheduletracker/entity/*`.  
  Effort: 1 day

## Milestone 2: CRUD for Teachers & Students
- **Teacher CRUD endpoints** (DONE)
  Modify `TeacherController` to restrict by role.
  Files: `TeacherController.java`, service implementations.
  Tests: controller tests.
  Effort: 2 days
- **Student CRUD endpoints** (DONE)
  Similar updates for manager role.
  Effort: 2 days

## Milestone 3: TimeSlot Management (DONE)
- **Entity and Repository**  
  Implement `TimeSlot` entity.(DONE)
  CRUD in `TimeSlotController` (DONE)
  Effort: 2 days

## Milestone 4: Lesson Scheduling Logic
- **Lesson endpoints with validation** (DONE)
  Ensure lessons cannot overlap teacher slots.
  Files: `LessonServiceImpl.java`, repository query methods.
  Tests for business rules.
  Effort: 3 days

## Milestone 5: Веб-интерфейс на Thymeleaf
- **Статические страницы**
  Создать шаблоны `login.html`, `manager.html`, `teacher.html` под `src/main/resources/templates`.
  Effort: 2 days
- **Формы и контроллеры**
  Обработчики входа и базовые страницы управления расписанием.
  Effort: 3 days

## Milestone 6: Дополнительные функции UI
- **Управление преподавателями и студентами**
  Отдельные страницы для CRUD операций.
  Effort: 3 days
- **Календарь занятий**
  Вывод расписания на страницах преподавателя и менеджера.
  Effort: 3 days

## Milestone 7: Manager Dashboard
- **Назначение студентов**
  Возможность привязывать студентов к преподавателям через веб-интерфейс.
  Effort: 2 days

## Milestone 8: Testing & Deployment
- **Backend tests reaching 80% coverage**
  Effort: 1 day
- **UI integration tests**
  Effort: 2 days
- **Docker and CI configuration** (DONE)
  `Dockerfile`, `docker-compose.yml` и workflow `deploy.yml` настроены для
  автоматического развёртывания на сервер при пуше в `main`.
  Effort: 1 day

