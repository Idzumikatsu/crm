package com.example.scheduletracker.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.scheduletracker.entity.Student;
import com.example.scheduletracker.repository.StudentRepository;
import com.example.scheduletracker.service.impl.StudentServiceImpl;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StudentServiceImplTest {

  @Mock private StudentRepository repo;

  private StudentService service;

  @BeforeEach
  void setup() {
    service = new StudentServiceImpl(repo);
  }

  @Test
  void findAllReturnsRepoData() {
    Student s = new Student(1L, "Bob", "b@example.com");
    when(repo.findAll()).thenReturn(List.of(s));

    List<Student> result = service.findAll();

    assertEquals(1, result.size());
    verify(repo).findAll();
  }
}
