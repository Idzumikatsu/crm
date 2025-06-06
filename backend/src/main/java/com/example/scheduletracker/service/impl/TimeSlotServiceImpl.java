package com.example.scheduletracker.service.impl;

import com.example.scheduletracker.entity.Teacher;
import com.example.scheduletracker.entity.TimeSlot;
import com.example.scheduletracker.repository.TeacherRepository;
import com.example.scheduletracker.repository.TimeSlotRepository;
import com.example.scheduletracker.service.TimeSlotService;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class TimeSlotServiceImpl implements TimeSlotService {
  private final TimeSlotRepository repo;
  private final TeacherRepository teacherRepository;

  public TimeSlotServiceImpl(
      TimeSlotRepository repo, TeacherRepository teacherRepository) {
    this.repo = repo;
    this.teacherRepository = teacherRepository;
  }

  @Override
  public TimeSlot save(TimeSlot slot) {
    return repo.save(slot);
  }

  @Override
  public List<TimeSlot> findAll() {
    return repo.findAll();
  }

  @Override
  public List<TimeSlot> findByTeacherId(Long teacherId) {
    Teacher t = teacherRepository.findById(teacherId).orElse(null);
    if (t == null) return List.of();
    return repo.findByTeacher(t);
  }

  @Override
  public Optional<TimeSlot> findById(Long id) {
    return repo.findById(id);
  }

  @Override
  public void deleteById(Long id) {
    repo.deleteById(id);
  }
}
