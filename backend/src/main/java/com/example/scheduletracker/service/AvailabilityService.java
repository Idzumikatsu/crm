package com.example.scheduletracker.service;

import com.example.scheduletracker.entity.AvailabilityTemplate;
import com.example.scheduletracker.entity.TimeSlot;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AvailabilityService {
  AvailabilityTemplate saveTemplate(AvailabilityTemplate template);

  List<AvailabilityTemplate> findTemplates(UUID teacherId);

  Optional<AvailabilityTemplate> findTemplate(UUID id);

  void deleteTemplate(UUID id);

  List<TimeSlot> generateSlots(UUID teacherId, LocalDate from, LocalDate to);

  void deleteSlots(UUID teacherId, LocalDate from);
}
