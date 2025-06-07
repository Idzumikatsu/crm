package com.example.scheduletracker.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.scheduletracker.entity.Student;
import com.example.scheduletracker.entity.Teacher;
import com.example.scheduletracker.service.StudentService;
import com.example.scheduletracker.service.TeacherService;
import com.example.scheduletracker.service.TeacherStudentService;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ManagerController.class)
@AutoConfigureMockMvc(addFilters = false)
class ManagerControllerTest {

  @Autowired private MockMvc mvc;

  @MockBean private TeacherService teacherService;
  @MockBean private StudentService studentService;
  @MockBean private TeacherStudentService teacherStudentService;
  @MockBean private com.example.scheduletracker.config.jwt.JwtUtils utils;

  @Test
  @DisplayName("GET /api/manager/teachers returns list")
  void teachersList() throws Exception {
    Teacher t = new Teacher(java.util.UUID.randomUUID(), "T1", null, "RUB");
    when(teacherService.findAll()).thenReturn(List.of(t));

    mvc.perform(get("/api/manager/teachers"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").value(t.getId().toString()));
  }

  @Test
  @DisplayName("POST /api/manager/assign assigns student")
  void assignCreatesRelation() throws Exception {
    Teacher t = new Teacher(java.util.UUID.randomUUID(), "T1", null, "RUB");
    Student s = new Student(java.util.UUID.randomUUID(), "S1", "s@e.com");
    when(teacherService.findById(t.getId())).thenReturn(Optional.of(t));
    when(studentService.findById(s.getId())).thenReturn(Optional.of(s));
    when(teacherStudentService.save(any())).thenAnswer(inv -> inv.getArgument(0));

    mvc.perform(post("/api/manager/assign?teacherId=" + t.getId() + "&studentId=" + s.getId()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.teacher.id").value(t.getId().toString()))
        .andExpect(jsonPath("$.student.id").value(s.getId().toString()));
  }
}
