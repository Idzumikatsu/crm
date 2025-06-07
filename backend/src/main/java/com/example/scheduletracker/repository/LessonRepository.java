// LessonRepository.java
package com.example.scheduletracker.repository;

import com.example.scheduletracker.entity.Lesson;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.example.scheduletracker.dto.LessonCountDto;

public interface LessonRepository extends JpaRepository<Lesson, UUID> {
  List<Lesson> findByDateTimeBetween(OffsetDateTime from, OffsetDateTime to);

  List<Lesson> findByTeacher(com.example.scheduletracker.entity.Teacher teacher);

  @Query("SELECT new com.example.scheduletracker.dto.LessonCountDto(t.id, t.name, COUNT(l)) " +
         "FROM Lesson l JOIN l.teacher t GROUP BY t.id, t.name")
  List<LessonCountDto> countLessonsPerTeacher();
}
