package com.example.scheduletracker.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.scheduletracker.entity.Teacher;
import com.example.scheduletracker.entity.TimeSlot;
import com.example.scheduletracker.repository.TeacherRepository;
import com.example.scheduletracker.repository.TimeSlotRepository;
import com.example.scheduletracker.service.impl.TimeSlotServiceImpl;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TimeSlotServiceImplTest {

  @Mock private TimeSlotRepository repo;
  @Mock private TeacherRepository teacherRepo;

  private TimeSlotService service;

  @BeforeEach
  void setup() {
    service = new TimeSlotServiceImpl(repo, teacherRepo);
  }

  @Test
  void findByTeacherIdReturnsEmptyWhenTeacherMissing() {
    when(teacherRepo.findById(1L)).thenReturn(Optional.empty());

    List<TimeSlot> result = service.findByTeacherId(1L);

    assertTrue(result.isEmpty());
    verify(repo, never()).findByTeacher(any());
  }

  @Test
  void findByTeacherIdReturnsRepoData() {
    Teacher t = new Teacher(1L, "T1", null, "RUB");
    TimeSlot slot = new TimeSlot(1L, t, null, null);
    when(teacherRepo.findById(1L)).thenReturn(Optional.of(t));
    when(repo.findByTeacher(t)).thenReturn(List.of(slot));

    List<TimeSlot> result = service.findByTeacherId(1L);

    assertEquals(1, result.size());
    assertEquals(1L, result.get(0).getId());
  }
}
