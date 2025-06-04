// LessonRepository.java
package com.example.scheduletracker.repository;

import com.example.scheduletracker.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface LessonRepository extends JpaRepository<Lesson, Long> {
    List<Lesson> findByDateTimeBetween(LocalDateTime from, LocalDateTime to);

    List<Lesson> findByTeacher(com.example.scheduletracker.entity.Teacher teacher);
}
