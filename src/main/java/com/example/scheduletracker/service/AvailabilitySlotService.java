package com.example.scheduletracker.service;

import com.example.scheduletracker.entity.AvailabilitySlot;

import java.util.List;
import java.util.Optional;

public interface AvailabilitySlotService {
    AvailabilitySlot save(AvailabilitySlot slot);
    List<AvailabilitySlot> findAll();
    List<AvailabilitySlot> findByTeacherId(Long teacherId);
    Optional<AvailabilitySlot> findById(Long id);
    void deleteById(Long id);
}
