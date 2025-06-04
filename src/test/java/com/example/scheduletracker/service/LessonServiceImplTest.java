package com.example.scheduletracker.service;

import com.example.scheduletracker.entity.Group;
import com.example.scheduletracker.entity.Lesson;
import com.example.scheduletracker.entity.Teacher;
import com.example.scheduletracker.repository.LessonRepository;
import com.example.scheduletracker.service.impl.LessonServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LessonServiceImplTest {

    @Mock
    private LessonRepository repo;

    private LessonService service;

    private Teacher t1;
    private Teacher t2;
    private Group g1;

    @BeforeEach
    void setup() {
        service = new LessonServiceImpl(repo);
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
}
