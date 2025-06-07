package com.example.scheduletracker.controller;

import com.example.scheduletracker.entity.Lesson;
import com.example.scheduletracker.service.LessonService;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/teacher/lessons")
@PreAuthorize("hasAnyRole('TEACHER','ADMIN')")
public class TeacherLessonController {
  private final LessonService svc;

  public TeacherLessonController(LessonService svc) {
    this.svc = svc;
  }

  @GetMapping
  public List<Lesson> list(@RequestParam UUID teacherId) {
    return svc.search(null, null, teacherId, null);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Lesson> update(@PathVariable UUID id, @RequestBody Lesson lesson) {
    return svc.findById(id)
        .map(
            existing -> {
              lesson.setId(id);
              return ResponseEntity.ok(svc.save(lesson));
            })
        .orElse(ResponseEntity.notFound().build());
  }
}
