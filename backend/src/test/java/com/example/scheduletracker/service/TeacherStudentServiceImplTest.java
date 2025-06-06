package com.example.scheduletracker.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.example.scheduletracker.entity.TeacherStudent;
import com.example.scheduletracker.repository.TeacherStudentRepository;
import com.example.scheduletracker.service.impl.TeacherStudentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TeacherStudentServiceImplTest {

  @Mock private TeacherStudentRepository repo;

  private TeacherStudentService service;

  @BeforeEach
  void setup() {
    service = new TeacherStudentServiceImpl(repo);
  }

  @Test
  void saveReturnsRepoResult() {
    TeacherStudent ts = new TeacherStudent();
    when(repo.save(any(TeacherStudent.class))).thenReturn(ts);

    TeacherStudent result = service.save(ts);

    assertSame(ts, result);
    verify(repo).save(ts);
  }
}
