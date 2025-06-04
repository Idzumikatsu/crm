package com.example.scheduletracker.service.impl;

import com.example.scheduletracker.entity.AvailabilitySlot;
import com.example.scheduletracker.entity.Teacher;
import com.example.scheduletracker.repository.AvailabilitySlotRepository;
import com.example.scheduletracker.repository.TeacherRepository;
import com.example.scheduletracker.service.AvailabilitySlotService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AvailabilitySlotServiceImpl implements AvailabilitySlotService {
    private final AvailabilitySlotRepository repo;
    private final TeacherRepository teacherRepository;

    @Override
    public AvailabilitySlot save(AvailabilitySlot slot) {
        return repo.save(slot);
    }

    @Override
    public List<AvailabilitySlot> findAll() {
        return repo.findAll();
    }

    @Override
    public List<AvailabilitySlot> findByTeacherId(Long teacherId) {
        Teacher t = teacherRepository.findById(teacherId).orElse(null);
        if (t == null) return List.of();
        return repo.findByTeacher(t);
    }

    @Override
    public Optional<AvailabilitySlot> findById(Long id) {
        return repo.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        repo.deleteById(id);
    }
}
