package com.example.scheduletracker.repository;

import com.example.scheduletracker.entity.AvailabilityTemplate;
import com.example.scheduletracker.entity.Teacher;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AvailabilityTemplateRepository extends JpaRepository<AvailabilityTemplate, Long> {
  List<AvailabilityTemplate> findByTeacher(Teacher teacher);
}
