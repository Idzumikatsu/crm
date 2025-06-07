// src/main/java/com/example/scheduletracker/controller/TeacherController.java
package com.example.scheduletracker.controller;

import com.example.scheduletracker.entity.Teacher;
import com.example.scheduletracker.service.TeacherService;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/teachers")
@PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
public class TeacherController {
  private final TeacherService svc;

  public TeacherController(TeacherService svc) {
    this.svc = svc;
  }

  @GetMapping
  public List<Teacher> all() {
    return svc.findAll();
  }

  @GetMapping("/{id}")
  public ResponseEntity<Teacher> get(@PathVariable UUID id) {
    return svc.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  public Teacher create(@RequestBody Teacher teacher) {
    return svc.save(teacher);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Teacher> update(@PathVariable UUID id, @RequestBody Teacher t) {
    return svc.findById(id)
        .map(
            existing -> {
              t.setId(id);
              return ResponseEntity.ok(svc.save(t));
            })
        .orElse(ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable UUID id) {
    svc.deleteById(id);
    return ResponseEntity.noContent().build();
  }
}
