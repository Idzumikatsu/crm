// src/main/java/com/example/scheduletracker/controller/LessonController.java
package com.example.scheduletracker.controller;

import com.example.scheduletracker.entity.Lesson;
import com.example.scheduletracker.service.LessonService;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/lessons")
public class LessonController {
  private final LessonService svc;

  public LessonController(LessonService svc) {
    this.svc = svc;
  }

  @GetMapping
  public List<Lesson> all(
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
          OffsetDateTime from,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
          OffsetDateTime to,
      @RequestParam(required = false) UUID teacherId,
      @RequestParam(required = false) UUID groupId) {
    if (from != null || to != null || teacherId != null || groupId != null) {
      return svc.search(from, to, teacherId, groupId);
    }
    return svc.findAll();
  }

  @GetMapping("/{id}")
  public ResponseEntity<Lesson> get(@PathVariable UUID id) {
    return svc.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  public Lesson create(@RequestBody Lesson lesson) {
    return svc.book(
        lesson.getTeacher().getId(),
        lesson.getGroup().getId(),
        lesson.getDateTime(),
        lesson.getDuration());
  }

  @PutMapping("/{id}")
  public ResponseEntity<Lesson> update(@PathVariable UUID id, @RequestBody Lesson l) {
    return svc.findById(id)
        .map(
            existing -> {
              l.setId(id);
              return ResponseEntity.ok(svc.save(l));
            })
        .orElse(ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable UUID id) {
    svc.deleteById(id);
    return ResponseEntity.noContent().build();
  }

  @PatchMapping("/{id}/status")
  public ResponseEntity<Lesson> updateStatus(
      @PathVariable UUID id, @RequestParam Lesson.Status status) {
    return ResponseEntity.ok(svc.updateStatus(id, status));
  }
}
