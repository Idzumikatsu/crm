package com.example.scheduletracker.controller;

import com.example.scheduletracker.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc     // security filters enabled
class UserControllerSecurityTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService svc;

    @Test
    @DisplayName("GET /api/users/{username} без авторизации возвращает 401")
    void getUserUnauthorized() throws Exception {
        mvc.perform(get("/api/users/alice"))
                .andExpect(status().isUnauthorized());
    }
}
