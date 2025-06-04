// src/main/java/com/example/scheduletracker/service/LessonService.java
package com.example.scheduletracker.service;

import com.example.scheduletracker.entity.Lesson;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface LessonService {
    Lesson save(Lesson lesson);
    List<Lesson> findAll();
    Optional<Lesson> findById(Long id);
    void deleteById(Long id);
    List<Lesson> findBetween(LocalDateTime from, LocalDateTime to);

    Lesson updateStatus(Long id, Lesson.Status status);

    /**
     * Search lessons by optional filters. Any {@code null} parameter is ignored.
     *
     * @param from  start of the period (inclusive)
     * @param to    end of the period (inclusive)
     * @param teacherId filter by teacher id
     * @param groupId   filter by group id
     * @return list of lessons matching all provided filters
     */
    List<Lesson> search(LocalDateTime from, LocalDateTime to, Long teacherId, Long groupId);
}
