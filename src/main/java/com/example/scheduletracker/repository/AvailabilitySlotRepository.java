package com.example.scheduletracker.repository;

import com.example.scheduletracker.entity.AvailabilitySlot;
import com.example.scheduletracker.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AvailabilitySlotRepository extends JpaRepository<AvailabilitySlot, Long> {
    List<AvailabilitySlot> findByTeacher(Teacher teacher);
}
