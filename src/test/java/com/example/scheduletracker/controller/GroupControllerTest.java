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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(GroupController.class)
@AutoConfigureMockMvc(addFilters = false)
class GroupControllerTest {

  @Autowired private MockMvc mvc;

  @MockBean private GroupService svc;

  @MockBean private com.example.scheduletracker.config.jwt.JwtUtils utils;

  @Test
  @DisplayName("GET /api/groups возвращает список групп")
  void allReturnsGroups() throws Exception {
    Group g = Group.builder().id(1L).name("G1").build();
    when(svc.findAll()).thenReturn(List.of(g));

    mvc.perform(get("/api/groups"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").value(1L));
  }

  @Test
  @DisplayName("GET /api/groups/{id} при отсутствии возвращает 404")
  void getNotFound() throws Exception {
    when(svc.findById(1L)).thenReturn(Optional.empty());

    mvc.perform(get("/api/groups/1")).andExpect(status().isNotFound());
  }
}
