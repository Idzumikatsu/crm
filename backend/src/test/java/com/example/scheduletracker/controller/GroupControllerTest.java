package com.example.scheduletracker.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.scheduletracker.entity.Group;
import com.example.scheduletracker.service.GroupService;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(GroupController.class)
@AutoConfigureMockMvc(addFilters = false)
class GroupControllerTest {

  @Autowired private MockMvc mvc;

  @MockitoBean private GroupService svc;

  @MockitoBean private com.example.scheduletracker.config.jwt.JwtUtils utils;

  @Test
  @DisplayName("GET /api/groups возвращает список групп")
  void allReturnsGroups() throws Exception {
    Group g = new Group(java.util.UUID.randomUUID(), "G1", null);
    when(svc.findAll()).thenReturn(List.of(g));

    mvc.perform(get("/api/groups"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").value(g.getId().toString()));
  }

  @Test
  @DisplayName("GET /api/groups/{id} при отсутствии возвращает 404")
  void getNotFound() throws Exception {
    when(svc.findById(java.util.UUID.fromString("00000000-0000-0000-0000-000000000001")))
        .thenReturn(Optional.empty());
    mvc.perform(get("/api/groups/00000000-0000-0000-0000-000000000001"))
        .andExpect(status().isNotFound());
  }
}
