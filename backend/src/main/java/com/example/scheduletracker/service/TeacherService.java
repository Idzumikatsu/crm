// src/main/java/com/example/scheduletracker/service/TeacherService.java
package com.example.scheduletracker.service;

import com.example.scheduletracker.entity.Teacher;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TeacherService {
  Teacher save(Teacher teacher);

  List<Teacher> findAll();

  Optional<Teacher> findById(UUID id);

  void deleteById(UUID id);
}
