// src/main/java/com/example/scheduletracker/service/LessonService.java
package com.example.scheduletracker.service;

import com.example.scheduletracker.entity.Lesson;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LessonService {
  Lesson save(Lesson lesson);

  List<Lesson> findAll();

  Optional<Lesson> findById(UUID id);

  void deleteById(UUID id);

  List<Lesson> findBetween(OffsetDateTime from, OffsetDateTime to);

  Lesson updateStatus(UUID id, Lesson.Status status);

  Lesson book(UUID teacherId, UUID groupId, OffsetDateTime start, int duration);

  /**
   * Search lessons by optional filters. Any {@code null} parameter is ignored.
   *
   * @param from start of the period (inclusive)
   * @param to end of the period (inclusive)
   * @param teacherId filter by teacher id
   * @param groupId filter by group id
   * @return list of lessons matching all provided filters
   */
  List<Lesson> search(OffsetDateTime from, OffsetDateTime to, UUID teacherId, UUID groupId);
}
