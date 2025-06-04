package com.example.scheduletracker.service;

import com.example.scheduletracker.entity.Student;
import com.example.scheduletracker.repository.StudentRepository;
import com.example.scheduletracker.service.impl.StudentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceImplTest {

    @Mock
    private StudentRepository repo;

    private StudentService service;

    @BeforeEach
    void setup() {
        service = new StudentServiceImpl(repo);
    }

    @Test
    void findAllReturnsRepoData() {
        Student s = Student.builder().id(1L).name("Bob").email("b@example.com").build();
        when(repo.findAll()).thenReturn(List.of(s));

        List<Student> result = service.findAll();

        assertEquals(1, result.size());
        verify(repo).findAll();
    }
}
