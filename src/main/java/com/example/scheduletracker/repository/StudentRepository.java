package com.example.scheduletracker.repository;

import com.example.scheduletracker.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {}
