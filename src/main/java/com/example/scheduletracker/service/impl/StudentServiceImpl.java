package com.example.scheduletracker.service.impl;

import com.example.scheduletracker.entity.Student;
import com.example.scheduletracker.repository.StudentRepository;
import com.example.scheduletracker.service.StudentService;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class StudentServiceImpl implements StudentService {
  private final StudentRepository repo;

  public StudentServiceImpl(StudentRepository repo) {
    this.repo = repo;
  }

  @Override
  public Student save(Student student) {
    return repo.save(student);
  }

  @Override
  public List<Student> findAll() {
    return repo.findAll();
  }

  @Override
  public Optional<Student> findById(Long id) {
    return repo.findById(id);
  }

  @Override
  public void deleteById(Long id) {
    repo.deleteById(id);
  }
}
