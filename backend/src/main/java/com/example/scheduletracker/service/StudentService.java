package com.example.scheduletracker.service;

import com.example.scheduletracker.entity.Student;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StudentService {
  Student save(Student student);

  List<Student> findAll();

  Optional<Student> findById(UUID id);

  void deleteById(UUID id);
}
