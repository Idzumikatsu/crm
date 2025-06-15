package com.example.scheduletracker.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.scheduletracker.entity.TeacherSettings;
import com.example.scheduletracker.entity.User;
import com.example.scheduletracker.service.TeacherSettingsService;
import com.example.scheduletracker.service.UserService;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(SettingsController.class)
@AutoConfigureMockMvc(addFilters = false)
class SettingsControllerTest {
  @Autowired private MockMvc mvc;

  @MockitoBean private TeacherSettingsService service;
  @MockitoBean private UserService userService;

  @Test
  void getReturnsSettings() throws Exception {
    UUID id = UUID.randomUUID();
    when(userService.findByUsername("alice"))
        .thenReturn(Optional.of(new User(id, "alice", "p", User.Role.TEACHER, null, false, true)));
    when(service.findByTeacherId(id)).thenReturn(Optional.of(new TeacherSettings(id, 5, "hi")));

    mvc.perform(get("/api/settings").principal(() -> "alice"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.bufferMin").value(5))
        .andExpect(jsonPath("$.template").value("hi"));
  }

  @Test
  void putUpdatesSettings() throws Exception {
    UUID id = UUID.randomUUID();
    when(userService.findByUsername("bob"))
        .thenReturn(Optional.of(new User(id, "bob", "p", User.Role.TEACHER, null, false, true)));
    TeacherSettings ts = new TeacherSettings(id, 10, "ok");
    when(service.save(ts)).thenReturn(ts);

    mvc.perform(
            put("/api/settings")
                .principal(() -> "bob")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"bufferMin\":10,\"template\":\"ok\"}"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.bufferMin").value(10));
  }
}
