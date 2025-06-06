// TeacherRepository.javaa
package com.example.scheduletracker.repository;

import com.example.scheduletracker.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {}
