package com.example.scheduletracker.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.scheduletracker.config.SecurityConfig;
import com.example.scheduletracker.config.jwt.JwtUtils;
import com.example.scheduletracker.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc // security filters enabled
@Import(SecurityConfig.class)
class UserControllerSecurityTest {

  @Autowired private MockMvc mvc;

  @MockitoBean private UserService svc;
  @MockitoBean private JwtUtils utils;
  @MockitoBean private UserDetailsService userDetailsService;

  @Test
  @DisplayName("GET /api/users/{username} без авторизации возвращает 401")
  void getUserUnauthorized() throws Exception {
    mvc.perform(get("/api/users/alice")).andExpect(status().isUnauthorized());
  }
}
