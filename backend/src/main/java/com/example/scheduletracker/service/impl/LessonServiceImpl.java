// src/main/java/com/example/scheduletracker/service/impl/LessonServiceImpl.java
package com.example.scheduletracker.service.impl;

import com.example.scheduletracker.entity.Group;
import com.example.scheduletracker.entity.Lesson;
import com.example.scheduletracker.entity.TimeSlot;
import com.example.scheduletracker.repository.LessonRepository;
import com.example.scheduletracker.repository.TimeSlotRepository;
import com.example.scheduletracker.service.LessonService;
import com.example.scheduletracker.exception.BookingConflictException;
import java.util.UUID;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LessonServiceImpl implements LessonService {
  private final LessonRepository repo;
  private final TimeSlotRepository slotRepo;

  public LessonServiceImpl(LessonRepository repo, TimeSlotRepository slotRepo) {
    this.repo = repo;
    this.slotRepo = slotRepo;
  }

  @Override
  public Lesson save(Lesson lesson) {
    var start = lesson.getDateTime();
    var end = start.plusMinutes(lesson.getDuration());

    // check that lesson fits teacher time slots
    List<TimeSlot> slots = slotRepo.findByTeacher(lesson.getTeacher());
    boolean withinSlot =
        slots.stream().anyMatch(s -> !s.getStart().isAfter(start) && !s.getEndTime().isBefore(end));
    if (!withinSlot) {
      throw new IllegalArgumentException("Lesson time outside teacher slots");
    }

    // check overlap with existing lessons of the teacher
    List<Lesson> existing = repo.findByTeacher(lesson.getTeacher());
    boolean overlaps =
        existing.stream()
            .anyMatch(
                l -> {
                  var s = l.getDateTime();
                  var e = s.plusMinutes(l.getDuration());
                  return s.isBefore(end) && e.isAfter(start) && !l.getId().equals(lesson.getId());
                });
    if (overlaps) {
      throw new IllegalStateException("Lesson overlaps existing lesson");
    }

    return repo.save(lesson);
  }

  @Override
  @Transactional
  public Lesson book(UUID teacherId, UUID groupId, OffsetDateTime start, int duration) {
    var end = start.plusMinutes(duration);

    var slot =
        slotRepo.findSlotForPeriodLocked(teacherId, start, end)
            .orElseThrow(() -> new BookingConflictException("slot not available"));

    Lesson lesson =
        Lesson.builder()
            .teacher(slot.getTeacher())
            .group(Group.builder().id(groupId).build())
            .dateTime(start)
            .duration(duration)
            .status(Lesson.Status.SCHEDULED)
            .build();

    // reuse save checks
    return save(lesson);
  }

  @Override
  public List<Lesson> findAll() {
    return repo.findAll();
  }

  @Override
  public Optional<Lesson> findById(UUID id) {
    return repo.findById(id);
  }

  @Override
  public void deleteById(UUID id) {
    repo.deleteById(id);
  }

  @Override
  public List<Lesson> findBetween(OffsetDateTime from, OffsetDateTime to) {
    return repo.findByDateTimeBetween(from, to);
  }

  @Override
  public List<Lesson> search(OffsetDateTime from, OffsetDateTime to, UUID teacherId, UUID groupId) {
    return repo.findAll().stream()
        .filter(l -> from == null || !l.getDateTime().isBefore(from))
        .filter(l -> to == null || !l.getDateTime().isAfter(to))
        .filter(
            l ->
                teacherId == null
                    || (l.getTeacher() != null && teacherId.equals(l.getTeacher().getId())))
        .filter(
            l -> groupId == null || (l.getGroup() != null && groupId.equals(l.getGroup().getId())))
        .toList();
  }

  @Override
  public Lesson updateStatus(UUID id, Lesson.Status status) {
    Lesson lesson = repo.findById(id).orElseThrow();
    lesson.setStatus(status);
    return repo.save(lesson);
  }
}
