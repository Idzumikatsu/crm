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
}
