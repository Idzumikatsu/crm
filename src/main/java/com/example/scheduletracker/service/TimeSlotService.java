package com.example.scheduletracker.service;

import com.example.scheduletracker.entity.TimeSlot;

import java.util.List;
import java.util.Optional;

public interface TimeSlotService {
    TimeSlot save(TimeSlot slot);
    List<TimeSlot> findAll();
    List<TimeSlot> findByTeacherId(Long teacherId);
    Optional<TimeSlot> findById(Long id);
    void deleteById(Long id);
}
