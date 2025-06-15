package com.example.scheduletracker.controller;

import com.example.scheduletracker.entity.TeacherSettings;
import com.example.scheduletracker.entity.User;
import com.example.scheduletracker.service.TeacherSettingsService;
import com.example.scheduletracker.service.UserService;
import java.util.Optional;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/settings")
public class SettingsController {
  private final TeacherSettingsService service;
  private final UserService userService;

  public SettingsController(TeacherSettingsService service, UserService userService) {
    this.service = service;
    this.userService = userService;
  }

  @GetMapping
  public ResponseEntity<TeacherSettings> get(@RequestParam UUID teacherId) {
    return service
        .findByTeacherId(teacherId)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.ok(new TeacherSettings(teacherId, null, null)));
  }

  @PutMapping
  public ResponseEntity<TeacherSettings> put(@RequestParam UUID teacherId, @RequestBody TeacherSettings dto) {
    dto.setTeacherId(teacherId);
    TeacherSettings saved = service.save(dto);
    return ResponseEntity.ok(saved);
  }
}
