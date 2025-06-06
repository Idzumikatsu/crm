package com.example.scheduletracker.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.scheduletracker.entity.AvailabilityTemplate;
import com.example.scheduletracker.entity.Teacher;
import com.example.scheduletracker.entity.TimeSlot;
import com.example.scheduletracker.repository.AvailabilityTemplateRepository;
import com.example.scheduletracker.repository.TeacherRepository;
import com.example.scheduletracker.repository.TimeSlotRepository;
import com.example.scheduletracker.service.impl.AvailabilityServiceImpl;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AvailabilityServiceImplTest {

  @Mock private AvailabilityTemplateRepository templateRepo;
  @Mock private TimeSlotRepository slotRepo;
  @Mock private TeacherRepository teacherRepo;

  private AvailabilityService service;

  @BeforeEach
  void setup() {
    service = new AvailabilityServiceImpl(templateRepo, slotRepo, teacherRepo);
  }

  @Test
  void generateSlotsCreatesSlots() {
    Teacher t = new Teacher(1L, "T", null, "RUB");
    AvailabilityTemplate tpl =
        new AvailabilityTemplate(
            1L, t, DayOfWeek.MONDAY, LocalTime.of(10, 0), LocalTime.of(11, 0), null);
    when(teacherRepo.findById(1L)).thenReturn(Optional.of(t));
    when(templateRepo.findByTeacher(t)).thenReturn(List.of(tpl));
    when(slotRepo.save(any())).thenAnswer(inv -> inv.getArgument(0));

    List<TimeSlot> result =
        service.generateSlots(
            1L, LocalDate.of(2025, 6, 9), LocalDate.of(2025, 6, 10)); // Monday-Tuesday

    assertEquals(1, result.size());
    verify(slotRepo).save(any());
  }

  @Test
  void deleteSlotsRemovesFutureSlots() {
    Teacher t = new Teacher(1L, "T", null, "RUB");
    TimeSlot future =
        new TimeSlot(
            1L,
            t,
            OffsetDateTime.of(2025, 6, 10, 10, 0, 0, 0, ZoneOffset.UTC),
            OffsetDateTime.of(2025, 6, 10, 11, 0, 0, 0, ZoneOffset.UTC));
    TimeSlot past =
        new TimeSlot(
            2L,
            t,
            OffsetDateTime.of(2025, 6, 5, 10, 0, 0, 0, ZoneOffset.UTC),
            OffsetDateTime.of(2025, 6, 5, 11, 0, 0, 0, ZoneOffset.UTC));
    when(teacherRepo.findById(1L)).thenReturn(Optional.of(t));
    when(slotRepo.findByTeacher(t)).thenReturn(List.of(future, past));

    service.deleteSlots(1L, LocalDate.of(2025, 6, 8));

    verify(slotRepo).delete(future);
    verify(slotRepo, never()).delete(past);
  }
}
