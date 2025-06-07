package com.example.scheduletracker.service;

import com.example.scheduletracker.entity.TimeSlot;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TimeSlotService {
  TimeSlot save(TimeSlot slot);

  List<TimeSlot> findAll();

  List<TimeSlot> findByTeacherId(UUID teacherId);

  Optional<TimeSlot> findById(UUID id);

  void deleteById(UUID id);
}
