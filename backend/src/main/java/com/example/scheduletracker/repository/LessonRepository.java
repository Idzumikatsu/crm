// LessonRepository.java
package com.example.scheduletracker.repository;

import com.example.scheduletracker.entity.Lesson;
import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LessonRepository extends JpaRepository<Lesson, Long> {
  List<Lesson> findByDateTimeBetween(OffsetDateTime from, OffsetDateTime to);

  List<Lesson> findByTeacher(com.example.scheduletracker.entity.Teacher teacher);
}
