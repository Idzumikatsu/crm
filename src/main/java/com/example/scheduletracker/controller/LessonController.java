// src/main/java/com/example/scheduletracker/controller/LessonController.java
package com.example.scheduletracker.controller;

import com.example.scheduletracker.entity.Lesson;
import com.example.scheduletracker.service.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/lessons")
@RequiredArgsConstructor
public class LessonController {
    private final LessonService svc;

    @GetMapping
    public List<Lesson> all(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
            @RequestParam(required = false) Long teacherId,
            @RequestParam(required = false) Long groupId
    ) {
        if (from != null || to != null || teacherId != null || groupId != null) {
            return svc.search(from, to, teacherId, groupId);
        }
        return svc.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Lesson> get(@PathVariable Long id) {
        return svc.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Lesson create(@RequestBody Lesson lesson) {
        return svc.save(lesson);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Lesson> update(@PathVariable Long id, @RequestBody Lesson l) {
        return svc.findById(id)
                .map(existing -> {
                    l.setId(id);
                    return ResponseEntity.ok(svc.save(l));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        svc.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
