// src/main/java/com/example/scheduletracker/service/impl/TeacherServiceImpl.java
package com.example.scheduletracker.service.impl;

import com.example.scheduletracker.entity.Teacher;
import com.example.scheduletracker.repository.TeacherRepository;
import com.example.scheduletracker.service.TeacherService;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class TeacherServiceImpl implements TeacherService {
  private final TeacherRepository repo;

  public TeacherServiceImpl(TeacherRepository repo) {
    this.repo = repo;
  }

  @Override
  public Teacher save(Teacher teacher) {
    return repo.save(teacher);
  }

  @Override
  public List<Teacher> findAll() {
    return repo.findAll();
  }

  @Override
  public Optional<Teacher> findById(Long id) {
    return repo.findById(id);
  }

  @Override
  public void deleteById(Long id) {
    repo.deleteById(id);
  }
}
