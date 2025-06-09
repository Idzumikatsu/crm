package com.example.scheduletracker.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(SpaController.class)
@AutoConfigureMockMvc(addFilters = false)
class SpaControllerTest {

  @Autowired private MockMvc mvc;

  @MockBean private com.example.scheduletracker.config.jwt.JwtUtils utils;

  @Test
  @DisplayName("SPA routes forward to index.html")
  void spaRoutesForwardToIndex() throws Exception {
    mvc.perform(get("/calendar")).andExpect(status().isOk()).andExpect(forwardedUrl("/index.html"));
    mvc.perform(get("/calendar/day"))
        .andExpect(status().isOk())
        .andExpect(forwardedUrl("/index.html"));
    mvc.perform(get("/login")).andExpect(status().isOk()).andExpect(forwardedUrl("/index.html"));
  }
}
