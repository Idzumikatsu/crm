package com.example.scheduletracker.service;

import com.example.scheduletracker.entity.Group;
import com.example.scheduletracker.entity.Lesson;
import com.example.scheduletracker.entity.Teacher;
import com.example.scheduletracker.entity.TimeSlot;
import com.example.scheduletracker.repository.LessonRepository;
import com.example.scheduletracker.repository.TimeSlotRepository;
import com.example.scheduletracker.service.impl.LessonServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LessonServiceImplTest {

    @Mock
    private LessonRepository repo;
    @Mock
    private TimeSlotRepository slotRepo;

    private LessonService service;

    private Teacher t1;
    private Teacher t2;
    private Group g1;

    @BeforeEach
    void setup() {
        service = new LessonServiceImpl(repo, slotRepo);
        t1 = Teacher.builder().id(1L).name("T1").build();
        t2 = Teacher.builder().id(2L).name("T2").build();
        g1 = Group.builder().id(1L).name("G1").build();
    }

    @Test
    void searchByTeacherFiltersResults() {
        Lesson l1 = Lesson.builder().id(1L).teacher(t1).group(g1).dateTime(LocalDateTime.now()).duration(60).build();
        Lesson l2 = Lesson.builder().id(2L).teacher(t2).group(g1).dateTime(LocalDateTime.now()).duration(60).build();
        when(repo.findAll()).thenReturn(List.of(l1, l2));

        List<Lesson> result = service.search(null, null, 1L, null);

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getTeacher().getId());
    }

    @Test
    void updateStatusChangesEntity() {
        Lesson lesson = Lesson.builder().id(1L).teacher(t1).group(g1)
                .dateTime(LocalDateTime.now()).duration(60).status(Lesson.Status.SCHEDULED).build();
        when(repo.findById(1L)).thenReturn(java.util.Optional.of(lesson));
        when(repo.save(any(Lesson.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Lesson updated = service.updateStatus(1L, Lesson.Status.CONFIRMED);

        assertEquals(Lesson.Status.CONFIRMED, updated.getStatus());
    }

    @Test
    void saveOutsideSlotThrows() {
        Lesson lesson = Lesson.builder().teacher(t1).group(g1)
                .dateTime(LocalDateTime.now()).duration(60).build();
        when(slotRepo.findByTeacher(t1)).thenReturn(List.of());

        assertThrows(IllegalArgumentException.class, () -> service.save(lesson));
    }

    @Test
    void saveOverlappingThrows() {
        LocalDateTime dt = LocalDateTime.now();
        TimeSlot slot = TimeSlot.builder().teacher(t1).start(dt.minusMinutes(30)).endTime(dt.plusMinutes(90)).build();
        when(slotRepo.findByTeacher(t1)).thenReturn(List.of(slot));
        Lesson existing = Lesson.builder().id(2L).teacher(t1).group(g1).dateTime(dt).duration(60).build();
        when(repo.findByTeacher(t1)).thenReturn(List.of(existing));

        Lesson newLesson = Lesson.builder().teacher(t1).group(g1)
                .dateTime(dt.plusMinutes(30)).duration(60).build();

        assertThrows(IllegalStateException.class, () -> service.save(newLesson));
    }

    @Test
    void saveValidLessonPersists() {
        LocalDateTime dt = LocalDateTime.now();
        TimeSlot slot = TimeSlot.builder().teacher(t1).start(dt.minusMinutes(10)).endTime(dt.plusMinutes(70)).build();
        when(slotRepo.findByTeacher(t1)).thenReturn(List.of(slot));
        when(repo.findByTeacher(t1)).thenReturn(List.of());
        when(repo.save(any(Lesson.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Lesson lesson = Lesson.builder().teacher(t1).group(g1).dateTime(dt).duration(60).build();

        Lesson saved = service.save(lesson);
        assertEquals(dt, saved.getDateTime());
    }
}
