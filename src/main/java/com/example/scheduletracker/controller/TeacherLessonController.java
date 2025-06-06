package com.example.scheduletracker.controller;

import com.example.scheduletracker.entity.Lesson;
import com.example.scheduletracker.service.LessonService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/teacher/lessons")
@RequiredArgsConstructor
@PreAuthorize("hasRole('TEACHER')")
public class TeacherLessonController {
  private final LessonService svc;

  @GetMapping
  public List<Lesson> list(@RequestParam Long teacherId) {
    return svc.search(null, null, teacherId, null);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Lesson> update(@PathVariable Long id, @RequestBody Lesson lesson) {
    return svc.findById(id)
        .map(
            existing -> {
              lesson.setId(id);
              return ResponseEntity.ok(svc.save(lesson));
            })
        .orElse(ResponseEntity.notFound().build());
  }
}
