package com.example.scheduletracker.controller;

import com.example.scheduletracker.entity.Student;
import com.example.scheduletracker.entity.Teacher;
import com.example.scheduletracker.entity.TeacherStudent;
import com.example.scheduletracker.service.StudentService;
import com.example.scheduletracker.service.TeacherService;
import com.example.scheduletracker.service.TeacherStudentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ManagerController.class)
@AutoConfigureMockMvc(addFilters = false)
class ManagerControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private TeacherService teacherService;
    @MockBean
    private StudentService studentService;
    @MockBean
    private TeacherStudentService teacherStudentService;
    @MockBean
    private com.example.scheduletracker.config.jwt.JwtUtils utils;

    @Test
    @DisplayName("GET /api/manager/teachers returns list")
    void teachersList() throws Exception {
        Teacher t = Teacher.builder().id(1L).name("T1").build();
        when(teacherService.findAll()).thenReturn(List.of(t));

        mvc.perform(get("/api/manager/teachers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    @DisplayName("POST /api/manager/assign assigns student")
    void assignCreatesRelation() throws Exception {
        Teacher t = Teacher.builder().id(1L).name("T1").build();
        Student s = Student.builder().id(2L).name("S1").email("s@e.com").build();
        when(teacherService.findById(1L)).thenReturn(Optional.of(t));
        when(studentService.findById(2L)).thenReturn(Optional.of(s));
        when(teacherStudentService.save(any())).thenAnswer(inv -> inv.getArgument(0));

        mvc.perform(post("/api/manager/assign?teacherId=1&studentId=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.teacher.id").value(1L))
                .andExpect(jsonPath("$.student.id").value(2L));
    }
}
