package com.example.scheduletracker.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.scheduletracker.service.AnalyticsService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AnalyticsController.class)
class AnalyticsControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private AnalyticsService analyticsService;

  @Test
  void lessonsPerTeacher() throws Exception {
    Mockito.when(analyticsService.lessonCountPerTeacher()).thenReturn(java.util.List.of());
    mockMvc.perform(get("/api/analytics/lessons-per-teacher")).andExpect(status().isOk());
  }
}
