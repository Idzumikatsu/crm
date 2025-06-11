package com.example.scheduletracker.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.scheduletracker.entity.User;
import com.example.scheduletracker.mapper.UserMapper;
import com.example.scheduletracker.service.UserService;
import com.example.scheduletracker.service.security.TotpService;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTwoFaTest {
  @Autowired MockMvc mvc;
  @MockitoBean UserService svc;
  @MockitoBean UserMapper mapper;
  @MockitoBean TotpService totpService;
  @MockitoBean com.example.scheduletracker.config.jwt.JwtUtils utils;

  @Test
  void enableTwoFaReturnsSecret() throws Exception {
    User user = new User(null, "alice", "p", User.Role.STUDENT, null, false, true);
    when(svc.findByUsername("alice")).thenReturn(Optional.of(user));
    when(totpService.generateSecret()).thenReturn("sec");
    when(svc.update(any())).thenReturn(user);

    mvc.perform(post("/api/users/me/2fa/enable").principal(() -> "alice"))
        .andExpect(status().isOk())
        .andExpect(content().json("{\"secret\":\"sec\"}"));

    verify(svc).update(argThat(u -> u.isTwoFaEnabled() && u.getTwoFaSecret().equals("sec")));
  }

  @Test
  void disableTwoFaUpdatesUser() throws Exception {
    User user = new User(null, "bob", "p", User.Role.STUDENT, "s", true, true);
    when(svc.findByUsername("bob")).thenReturn(Optional.of(user));
    when(svc.update(any())).thenReturn(user);

    mvc.perform(post("/api/users/me/2fa/disable").principal(() -> "bob"))
        .andExpect(status().isOk());

    verify(svc).update(argThat(u -> !u.isTwoFaEnabled()));
  }
}
