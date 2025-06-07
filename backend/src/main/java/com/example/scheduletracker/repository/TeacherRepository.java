// TeacherRepository.javaa
package com.example.scheduletracker.repository;

import com.example.scheduletracker.entity.Teacher;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherRepository extends JpaRepository<Teacher, UUID> {}
