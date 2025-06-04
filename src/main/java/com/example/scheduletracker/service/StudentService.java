package com.example.scheduletracker.service;

import com.example.scheduletracker.entity.Student;

import java.util.List;
import java.util.Optional;

public interface StudentService {
    Student save(Student student);
    List<Student> findAll();
    Optional<Student> findById(Long id);
    void deleteById(Long id);
}
