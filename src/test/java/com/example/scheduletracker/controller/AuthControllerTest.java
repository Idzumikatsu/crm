package com.example.scheduletracker.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.scheduletracker.config.jwt.JwtUtils;
import com.example.scheduletracker.entity.User;
import com.example.scheduletracker.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {
  @Autowired MockMvc mvc;
  @MockBean AuthenticationManager authManager;
  @MockBean JwtUtils utils;
  @MockBean UserService userService;

  @Test
  void loginReturnsOk() throws Exception {
    Authentication auth = new UsernamePasswordAuthenticationToken("user", "pass");
    when(authManager.authenticate(any())).thenReturn(auth);
    when(userService.findByUsername("user"))
        .thenReturn(
            java.util.Optional.of(User.builder().username("user").role(User.Role.TEACHER).build()));
    when(utils.generateToken(any(), any())).thenReturn("token");

    mvc.perform(
            post("/api/auth/login")
                .contentType("application/json")
                .content("{\"username\":\"u\",\"password\":\"p\"}"))
        .andExpect(status().isOk());
  }

  @Test
  void registerCreatesUser() throws Exception {
    when(userService.findByUsername("new")).thenReturn(java.util.Optional.empty());
    when(userService.save(any())).thenReturn(User.builder().id(1L).build());

    mvc.perform(
            post("/api/auth/register")
                .contentType("application/json")
                .content("{\"username\":\"new\",\"password\":\"p\"}"))
        .andExpect(status().isCreated());
  }
}
