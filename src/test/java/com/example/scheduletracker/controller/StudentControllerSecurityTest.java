package com.example.scheduletracker.controller;

import com.example.scheduletracker.service.StudentService;
import com.example.scheduletracker.config.jwt.JwtUtils;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.context.annotation.Import;
import com.example.scheduletracker.config.SecurityConfig;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StudentController.class)
@AutoConfigureMockMvc
@Import(SecurityConfig.class)
class StudentControllerSecurityTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private StudentService svc;
    @MockBean
    private JwtUtils utils;
    @MockBean
    private UserDetailsService userDetailsService;

    @Test
    @DisplayName("GET /api/students без авторизации возвращает 401")
    void getAllUnauthorized() throws Exception {
        mvc.perform(get("/api/students"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    @DisplayName("GET /api/students с ролью TEACHER возвращает 403")
    void getAllForbiddenForTeacher() throws Exception {
        mvc.perform(get("/api/students"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    @DisplayName("GET /api/students с ролью MANAGER разрешено")
    void getAllAllowedForManager() throws Exception {
        mvc.perform(get("/api/students"))
                .andExpect(status().isOk());
    }
}
