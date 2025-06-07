package com.example.scheduletracker.repository;

import com.example.scheduletracker.entity.AvailabilityTemplate;
import com.example.scheduletracker.entity.Teacher;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AvailabilityTemplateRepository
    extends JpaRepository<AvailabilityTemplate, UUID> {
  List<AvailabilityTemplate> findByTeacher(Teacher teacher);
}
