package com.example.scheduletracker.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.scheduletracker.config.jwt.JwtUtils;
import com.example.scheduletracker.entity.User;
import com.example.scheduletracker.entity.VerificationToken;
import com.example.scheduletracker.repository.VerificationTokenRepository;
import com.example.scheduletracker.service.NotificationService;
import com.example.scheduletracker.service.UserService;
import com.example.scheduletracker.service.security.TotpService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {
  @Autowired MockMvc mvc;
  @MockitoBean AuthenticationManager authManager;
  @MockitoBean JwtUtils utils;
  @MockitoBean UserService userService;
  @MockitoBean TotpService totpService;
  @MockitoBean NotificationService notificationService;
  @MockitoBean VerificationTokenRepository tokenRepository;

  @Test
  void loginReturnsOk() throws Exception {
    Authentication auth = new UsernamePasswordAuthenticationToken("user", "pass");
    when(authManager.authenticate(any())).thenReturn(auth);
    when(userService.findByUsername("user"))
        .thenReturn(
            java.util.Optional.of(
                new User(null, "user", null, User.Role.TEACHER, "s", true, true)));
    when(totpService.verifyCode("s", "123456")).thenReturn(true);
    when(utils.generateToken(any(), any())).thenReturn("token");

    mvc.perform(
            post("/api/auth/login")
                .contentType("application/json")
                .content("{\"username\":\"u\",\"password\":\"p\",\"code\":\"123456\"}"))
        .andExpect(status().isOk());
  }

  @Test
  void loginWithout2faSkipsVerification() throws Exception {
    Authentication auth = new UsernamePasswordAuthenticationToken("user", "pass");
    when(authManager.authenticate(any())).thenReturn(auth);
    when(userService.findByUsername("user"))
        .thenReturn(
            java.util.Optional.of(
                new User(null, "user", null, User.Role.TEACHER, "s", false, true)));
    when(utils.generateToken(any(), any())).thenReturn("t");

    mvc.perform(
            post("/api/auth/login")
                .contentType("application/json")
                .content("{\"username\":\"u\",\"password\":\"p\",\"code\":\"000000\"}"))
        .andExpect(status().isOk());

    verify(totpService, never()).verifyCode(any(), any());
  }

  @Test
  void registerCreatesUser() throws Exception {
    when(userService.findByUsername("new")).thenReturn(java.util.Optional.empty());
    when(totpService.generateSecret()).thenReturn("secret");
    when(userService.save(any()))
        .thenReturn(
            new User(
                java.util.UUID.randomUUID(),
                "new",
                "p",
                User.Role.STUDENT,
                "secret",
                false,
                false));
    when(tokenRepository.save(any())).thenReturn(null);

    mvc.perform(
            post("/api/auth/register")
                .contentType("application/json")
                .content("{\"username\":\"new\",\"password\":\"p\"}"))
        .andExpect(status().isCreated());

    verify(notificationService)
        .sendEmail(eq("new"), eq("Verify account"), startsWith("/api/auth/verify?token="));
  }

  @Test
  void loginWithShortCodeFails() throws Exception {
    mvc.perform(
            post("/api/auth/login")
                .contentType("application/json")
                .content("{\"username\":\"u\",\"password\":\"p\",\"code\":\"123\"}"))
        .andExpect(status().isBadRequest());
  }

  @Test
  void registerWithoutUsernameFails() throws Exception {
    mvc.perform(
            post("/api/auth/register")
                .contentType("application/json")
                .content("{\"password\":\"p\"}"))
        .andExpect(status().isBadRequest());
  }

  @Test
  void loginRejectsUnverifiedUser() throws Exception {
    Authentication auth = new UsernamePasswordAuthenticationToken("user", "pass");
    when(authManager.authenticate(any())).thenReturn(auth);
    when(userService.findByUsername("user"))
        .thenReturn(
            java.util.Optional.of(
                new User(null, "user", null, User.Role.STUDENT, null, false, false)));

    mvc.perform(
            post("/api/auth/login")
                .contentType("application/json")
                .content("{\"username\":\"u\",\"password\":\"p\",\"code\":\"000000\"}"))
        .andExpect(status().isUnauthorized());
  }

  @Test
  void verifyActivatesUser() throws Exception {
    java.util.UUID token = java.util.UUID.randomUUID();
    User user = new User(null, "u", "p", User.Role.STUDENT, null, false, false);
    when(tokenRepository.findByToken(token))
        .thenReturn(java.util.Optional.of(new VerificationToken(token, user)));

    mvc.perform(get("/api/auth/verify").param("token", token.toString()))
        .andExpect(status().isOk());

    verify(userService).update(argThat(u -> u.isActive()));
    verify(tokenRepository).delete(any());
  }
}
