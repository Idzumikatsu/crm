package com.example.scheduletracker.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.scheduletracker.entity.Group;
import com.example.scheduletracker.entity.Lesson;
import com.example.scheduletracker.entity.Teacher;
import com.example.scheduletracker.entity.TimeSlot;
import com.example.scheduletracker.exception.BookingConflictException;
import com.example.scheduletracker.repository.LessonRepository;
import com.example.scheduletracker.repository.TimeSlotRepository;
import com.example.scheduletracker.service.impl.LessonServiceImpl;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LessonServiceImplTest {

  @Mock private LessonRepository repo;
  @Mock private TimeSlotRepository slotRepo;

  private LessonService service;

  private Teacher t1;
  private Teacher t2;
  private Group g1;

  @BeforeEach
  void setup() {
    service = new LessonServiceImpl(repo, slotRepo);
    t1 = new Teacher(1L, "T1", null);
    t2 = new Teacher(2L, "T2", null);
    g1 = new Group(1L, "G1", null);
  }

  @Test
  void searchByTeacherFiltersResults() {
    Lesson l1 = new Lesson(1L, OffsetDateTime.now(), 60, Lesson.Status.SCHEDULED, t1, g1);
    Lesson l2 = new Lesson(2L, OffsetDateTime.now(), 60, Lesson.Status.SCHEDULED, t2, g1);
    when(repo.findAll()).thenReturn(List.of(l1, l2));

    List<Lesson> result = service.search(null, null, 1L, null);

    assertEquals(1, result.size());
    assertEquals(1L, result.get(0).getTeacher().getId());
  }

  @Test
  void updateStatusChangesEntity() {
    Lesson lesson = new Lesson(1L, OffsetDateTime.now(), 60, Lesson.Status.SCHEDULED, t1, g1);
    when(repo.findById(1L)).thenReturn(java.util.Optional.of(lesson));
    when(repo.save(any(Lesson.class))).thenAnswer(invocation -> invocation.getArgument(0));

    Lesson updated = service.updateStatus(1L, Lesson.Status.CONFIRMED);

    assertEquals(Lesson.Status.CONFIRMED, updated.getStatus());
  }

  @Test
  void saveOutsideSlotThrows() {
    Lesson lesson = new Lesson(null, OffsetDateTime.now(), 60, Lesson.Status.SCHEDULED, t1, g1);
    when(slotRepo.findByTeacher(t1)).thenReturn(List.of());

    assertThrows(IllegalArgumentException.class, () -> service.save(lesson));
  }

  @Test
  void saveOverlappingThrows() {
    OffsetDateTime dt = OffsetDateTime.now();
    TimeSlot slot = new TimeSlot(null, t1, dt.minusMinutes(30), dt.plusMinutes(90));
    when(slotRepo.findByTeacher(t1)).thenReturn(List.of(slot));
    Lesson existing = new Lesson(2L, dt, 60, Lesson.Status.SCHEDULED, t1, g1);
    when(repo.findByTeacher(t1)).thenReturn(List.of(existing));

    Lesson newLesson = new Lesson(null, dt.plusMinutes(30), 60, Lesson.Status.SCHEDULED, t1, g1);

    assertThrows(IllegalStateException.class, () -> service.save(newLesson));
  }

  @Test
  void saveValidLessonPersists() {
    OffsetDateTime dt = OffsetDateTime.now();
    TimeSlot slot = new TimeSlot(null, t1, dt.minusMinutes(10), dt.plusMinutes(70));
    when(slotRepo.findByTeacher(t1)).thenReturn(List.of(slot));
    when(repo.findByTeacher(t1)).thenReturn(List.of());
    when(repo.save(any(Lesson.class))).thenAnswer(invocation -> invocation.getArgument(0));

    Lesson lesson = new Lesson(null, dt, 60, Lesson.Status.SCHEDULED, t1, g1);

    Lesson saved = service.save(lesson);
    assertEquals(dt, saved.getDateTime());
  }

  @Test
  void bookWhenSlotMissingThrows() {
    OffsetDateTime dt = OffsetDateTime.now();
    when(slotRepo.findSlotForPeriodLocked(anyLong(), any(), any())).thenReturn(Optional.empty());

    assertThrows(
        BookingConflictException.class,
        () -> service.book(1L, 1L, dt, 60));
  }

  @Test
  void bookSuccessPersistsLesson() {
    OffsetDateTime dt = OffsetDateTime.now();
    TimeSlot slot = new TimeSlot(1L, t1, dt, dt.plusMinutes(60));
    when(slotRepo.findSlotForPeriodLocked(eq(1L), any(), any())).thenReturn(Optional.of(slot));
    when(slotRepo.findByTeacher(t1)).thenReturn(List.of(slot));
    when(repo.findByTeacher(t1)).thenReturn(List.of());
    when(repo.save(any(Lesson.class))).thenAnswer(invocation -> invocation.getArgument(0));

    Lesson booked = service.book(1L, 1L, dt, 60);
    assertEquals(dt, booked.getDateTime());
  }
}
