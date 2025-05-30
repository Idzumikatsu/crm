// src/main/java/com/example/scheduletracker/service/TeacherService.java
package com.example.scheduletracker.service;

import com.example.scheduletracker.entity.Teacher;
import java.util.List;
import java.util.Optional;

public interface TeacherService {
    Teacher save(Teacher teacher);
    List<Teacher> findAll();
    Optional<Teacher> findById(Long id);
    void deleteById(Long id);
}
