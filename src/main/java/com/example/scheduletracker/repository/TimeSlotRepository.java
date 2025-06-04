package com.example.scheduletracker.repository;

import com.example.scheduletracker.entity.TimeSlot;
import com.example.scheduletracker.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TimeSlotRepository extends JpaRepository<TimeSlot, Long> {
    List<TimeSlot> findByTeacher(Teacher teacher);
}
