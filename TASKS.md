# Project Task Breakdown

This file lists detailed sub-tasks for implementing the CRM application.

## Milestone 1: Project Setup & Authentication
- **Setup Spring Boot project**  
  Files: `pom.xml`, `src/**`  
  Effort: 1 day
- **Configure JWT authentication**  
  Add `JwtUtils` helper, `JwtFilter`, update `SecurityConfig`.  
  Create `AuthController` with `POST /api/auth/login`.  
  Files: `src/main/java/com/example/scheduletracker/config/*`, `src/main/java/com/example/scheduletracker/controller/AuthController.java`.  
  Tests: `AuthControllerTest`.  
  Effort: 2 days
- **Initial entities and migrations**  
  Create `TeacherStudent` join entity.  
  Ensure `User`, `Teacher`, `Student` tables exist via JPA/Hibernate ddl-auto or migrations.  
  Files: `src/main/java/com/example/scheduletracker/entity/*`.  
  Effort: 1 day

## Milestone 2: CRUD for Teachers & Students
- **Teacher CRUD endpoints**  
  Modify `TeacherController` to restrict by role.  
  Files: `TeacherController.java`, service implementations.  
  Tests: controller tests.  
  Effort: 2 days
- **Student CRUD endpoints**  
  Similar updates for manager role.  
  Effort: 2 days

## Milestone 3: TimeSlot Management
- **Entity and Repository**  
  Implement `TimeSlot` entity.  
  CRUD in `AvailabilitySlotController` (rename to `TimeSlotController`).  
  Effort: 2 days

## Milestone 4: Lesson Scheduling Logic
- **Lesson endpoints with validation**  
  Ensure lessons cannot overlap teacher slots.  
  Files: `LessonServiceImpl.java`, repository query methods.  
  Tests for business rules.  
  Effort: 3 days

## Milestone 5: Front-end Setup
- **Initialize React project under `/client`**  
  `package.json`, `src/index.jsx`, `App.jsx`.  
  Routing with React Router.  
  Effort: 1 day
- **Authentication flow**  
  Context or Redux store for auth.  
  Login form that posts to `/api/auth/login` and stores JWT.  
  Tests with React Testing Library.  
  Effort: 2 days

## Milestone 6: Teacher Dashboard
- **Student list component**  
  `StudentList.jsx`.  
  Effort: 1 day
- **Time slot management**  
  `TimeSlotForm.jsx`, `TimeSlotList.jsx`.  
  Effort: 2 days
- **Calendar integration**  
  Use `react-big-calendar` to display lessons.  
  Drag & drop support.  
  Effort: 3 days

## Milestone 7: Manager Dashboard
- **Teacher/Student management pages**  
  `TeacherTable.jsx`, `StudentTable.jsx`.  
  Effort: 3 days
- **Assignment UI**  
  Component for assigning students to teachers.  
  Effort: 2 days
- **Filtering calendar**  
  Dropdown filters in manager view.  
  Effort: 1 day

## Milestone 8: Testing & Deployment
- **Backend tests reaching 80% coverage**  
  Effort: 1 day
- **Frontend component & e2e tests**  
  Effort: 2 days
- **Docker and CI configuration**  
  `Dockerfile`, `docker-compose.yml`, GitHub Actions workflow.  
  Effort: 1 day

