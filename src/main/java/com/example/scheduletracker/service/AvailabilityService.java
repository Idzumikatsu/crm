package com.example.scheduletracker.service;

import com.example.scheduletracker.entity.AvailabilityTemplate;
import com.example.scheduletracker.entity.TimeSlot;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AvailabilityService {
  AvailabilityTemplate saveTemplate(AvailabilityTemplate template);

  List<AvailabilityTemplate> findTemplates(Long teacherId);

  Optional<AvailabilityTemplate> findTemplate(Long id);

  void deleteTemplate(Long id);

  List<TimeSlot> generateSlots(Long teacherId, LocalDate from, LocalDate to);

  void deleteSlots(Long teacherId, LocalDate from);
}
