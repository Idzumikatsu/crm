package com.example.scheduletracker.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.scheduletracker.config.SecurityConfig;
import com.example.scheduletracker.config.jwt.JwtUtils;
import com.example.scheduletracker.service.TeacherService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(TeacherController.class)
@AutoConfigureMockMvc
@Import(SecurityConfig.class)
class TeacherControllerSecurityTest {

  @Autowired private MockMvc mvc;

  @MockitoBean private TeacherService svc;
  @MockitoBean private JwtUtils utils;
  @MockitoBean private UserDetailsService userDetailsService;

  @Test
  @DisplayName("GET /api/teachers без авторизации возвращает 401")
  void getAllUnauthorized() throws Exception {
    mvc.perform(get("/api/teachers")).andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser(roles = "TEACHER")
  @DisplayName("GET /api/teachers с ролью TEACHER возвращает 403")
  void getAllForbiddenForTeacher() throws Exception {
    mvc.perform(get("/api/teachers")).andExpect(status().isForbidden());
  }

  @Test
  @WithMockUser(roles = "MANAGER")
  @DisplayName("GET /api/teachers с ролью MANAGER разрешено")
  void getAllAllowedForManager() throws Exception {
    mvc.perform(get("/api/teachers")).andExpect(status().isOk());
  }
}
