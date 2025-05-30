// src/main/java/com/example/scheduletracker/service/impl/LessonServiceImpl.java
package com.example.scheduletracker.service.impl;

import com.example.scheduletracker.entity.Lesson;
import com.example.scheduletracker.repository.LessonRepository;
import com.example.scheduletracker.service.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LessonServiceImpl implements LessonService {
    private final LessonRepository repo;

    @Override
    public Lesson save(Lesson lesson) {
        return repo.save(lesson);
    }

    @Override
    public List<Lesson> findAll() {
        return repo.findAll();
    }

    @Override
    public Optional<Lesson> findById(Long id) {
        return repo.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        repo.deleteById(id);
    }

    @Override
    public List<Lesson> findBetween(LocalDateTime from, LocalDateTime to) {
        return repo.findByDateTimeBetween(from, to);
    }
}
