package com.example.scheduletracker.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.example.scheduletracker.entity.Teacher;
import com.example.scheduletracker.repository.TeacherRepository;
import com.example.scheduletracker.service.impl.TeacherServiceImpl;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TeacherServiceImplTest {

  @Mock private TeacherRepository repo;

  private TeacherService service;

  @BeforeEach
  void setup() {
    service = new TeacherServiceImpl(repo);
  }

  @Test
  void saveDelegatesToRepo() {
    Teacher t = new Teacher(null, "T1", null, "RUB");
    when(repo.save(any(Teacher.class))).thenAnswer(invocation -> invocation.getArgument(0));

    Teacher saved = service.save(t);

    assertEquals("T1", saved.getName());
    verify(repo).save(t);
  }

  @Test
  void findByIdReturnsData() {
    java.util.UUID id = java.util.UUID.randomUUID();
    Teacher t = new Teacher(id, "T1", null, "RUB");
    when(repo.findById(id)).thenReturn(Optional.of(t));

    Optional<Teacher> result = service.findById(id);

    assertTrue(result.isPresent());
    assertEquals(id, result.get().getId());
  }

  @Test
  void findAllReturnsList() {
    when(repo.findAll()).thenReturn(List.of(new Teacher()));

    List<Teacher> result = service.findAll();

    assertEquals(1, result.size());
    verify(repo).findAll();
  }
}
