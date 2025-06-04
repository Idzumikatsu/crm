package com.example.scheduletracker.service;

import com.example.scheduletracker.entity.AvailabilitySlot;
import com.example.scheduletracker.entity.Teacher;
import com.example.scheduletracker.repository.AvailabilitySlotRepository;
import com.example.scheduletracker.repository.TeacherRepository;
import com.example.scheduletracker.service.impl.AvailabilitySlotServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AvailabilitySlotServiceImplTest {

    @Mock
    private AvailabilitySlotRepository repo;
    @Mock
    private TeacherRepository teacherRepo;

    private AvailabilitySlotService service;

    @BeforeEach
    void setup() {
        service = new AvailabilitySlotServiceImpl(repo, teacherRepo);
    }

    @Test
    void findByTeacherIdReturnsEmptyWhenTeacherMissing() {
        when(teacherRepo.findById(1L)).thenReturn(Optional.empty());

        List<AvailabilitySlot> result = service.findByTeacherId(1L);

        assertTrue(result.isEmpty());
        verify(repo, never()).findByTeacher(any());
    }

    @Test
    void findByTeacherIdReturnsRepoData() {
        Teacher t = Teacher.builder().id(1L).name("T1").build();
        AvailabilitySlot slot = AvailabilitySlot.builder().id(1L).teacher(t).build();
        when(teacherRepo.findById(1L)).thenReturn(Optional.of(t));
        when(repo.findByTeacher(t)).thenReturn(List.of(slot));

        List<AvailabilitySlot> result = service.findByTeacherId(1L);

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
    }
}
