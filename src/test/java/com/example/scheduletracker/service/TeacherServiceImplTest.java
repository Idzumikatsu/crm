package com.example.scheduletracker.service;

import com.example.scheduletracker.entity.Teacher;
import com.example.scheduletracker.repository.TeacherRepository;
import com.example.scheduletracker.service.impl.TeacherServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TeacherServiceImplTest {

    @Mock
    private TeacherRepository repo;

    private TeacherService service;

    @BeforeEach
    void setup() {
        service = new TeacherServiceImpl(repo);
    }

    @Test
    void saveDelegatesToRepo() {
        Teacher t = Teacher.builder().name("T1").build();
        when(repo.save(any(Teacher.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Teacher saved = service.save(t);

        assertEquals("T1", saved.getName());
        verify(repo).save(t);
    }

    @Test
    void findByIdReturnsData() {
        Teacher t = Teacher.builder().id(1L).name("T1").build();
        when(repo.findById(1L)).thenReturn(Optional.of(t));

        Optional<Teacher> result = service.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
    }

    @Test
    void findAllReturnsList() {
        when(repo.findAll()).thenReturn(List.of(Teacher.builder().build()));

        List<Teacher> result = service.findAll();

        assertEquals(1, result.size());
        verify(repo).findAll();
    }
}
