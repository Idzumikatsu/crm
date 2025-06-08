package com.example.scheduletracker.service.impl;

import com.example.scheduletracker.entity.AvailabilityTemplate;
import com.example.scheduletracker.entity.Teacher;
import com.example.scheduletracker.entity.TimeSlot;
import com.example.scheduletracker.repository.AvailabilityTemplateRepository;
import com.example.scheduletracker.repository.TeacherRepository;
import com.example.scheduletracker.repository.TimeSlotRepository;
import com.example.scheduletracker.service.AvailabilityService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AvailabilityServiceImpl implements AvailabilityService {
  private final AvailabilityTemplateRepository templateRepository;
  private final TimeSlotRepository slotRepository;
  private final TeacherRepository teacherRepository;

  public AvailabilityServiceImpl(
      AvailabilityTemplateRepository templateRepository,
      TimeSlotRepository slotRepository,
      TeacherRepository teacherRepository) {
    this.templateRepository = templateRepository;
    this.slotRepository = slotRepository;
    this.teacherRepository = teacherRepository;
  }

  @Override
  public AvailabilityTemplate saveTemplate(AvailabilityTemplate template) {
    return templateRepository.save(template);
  }

  @Override
  public List<AvailabilityTemplate> findTemplates(UUID teacherId) {
    if (teacherId == null) {
      return templateRepository.findAll();
    }
    return teacherRepository
        .findById(teacherId)
        .map(templateRepository::findByTeacher)
        .orElse(List.of());
  }

  @Override
  public Optional<AvailabilityTemplate> findTemplate(UUID id) {
    return templateRepository.findById(id);
  }

  @Override
  public void deleteTemplate(UUID id) {
    templateRepository.deleteById(id);
  }

  @Override
  @Transactional
  public List<TimeSlot> generateSlots(UUID teacherId, LocalDate from, LocalDate to) {
    Teacher teacher =
        teacherRepository
            .findById(teacherId)
            .orElseThrow(() -> new IllegalArgumentException("teacher"));
    List<AvailabilityTemplate> templates = templateRepository.findByTeacher(teacher);
    List<TimeSlot> created = new ArrayList<>();
    LocalDate date = from;
    while (!date.isAfter(to)) {
      for (AvailabilityTemplate t : templates) {
        if (t.getWeekDay().getValue() % 7 == date.getDayOfWeek().getValue() % 7) {
          if (t.getUntilDate() != null && date.isAfter(t.getUntilDate())) {
            continue;
          }
          LocalDateTime startDateTime = date.atTime(t.getStartTime());
          LocalDateTime endDateTime = date.atTime(t.getEndTime());
          TimeSlot slot =
              TimeSlot.builder()
                  .teacher(teacher)
                  .startTs(startDateTime.atOffset(java.time.ZoneOffset.UTC))
                  .endTs(endDateTime.atOffset(java.time.ZoneOffset.UTC))
                  .build();
          created.add(slotRepository.save(slot));
        }
      }
      date = date.plusDays(1);
    }
    return created;
  }

  @Override
  @Transactional
  public void deleteSlots(UUID teacherId, LocalDate from) {
    Teacher teacher =
        teacherRepository
            .findById(teacherId)
            .orElseThrow(() -> new IllegalArgumentException("teacher"));
    List<TimeSlot> slots = slotRepository.findByTeacher(teacher);
    for (TimeSlot s : slots) {
      if (!s.getStartTs().toLocalDate().isBefore(from)) {
        slotRepository.delete(s);
      }
    }
  }
}
