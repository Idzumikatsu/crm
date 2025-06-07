package com.example.scheduletracker.service.impl;

import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

class TelegramNotificationServiceTest {

    private MockRestServiceServer server;
    private TelegramNotificationService service;

    @BeforeEach
    void setup() {
        RestTemplate rest = new RestTemplate();
        server = MockRestServiceServer.createServer(rest);
        service = new TelegramNotificationService(rest, "token");
    }

    @Test
    void sendTelegramPostsMessage() {
        server.expect(once(), requestTo("https://api.telegram.org/bottoken/sendMessage"))
                .andExpect(method(org.springframework.http.HttpMethod.POST))
                .andExpect(content().json("{\"chat_id\":\"1\",\"text\":\"hi\"}"))
                .andRespond(withSuccess("{}", MediaType.APPLICATION_JSON));

        service.sendTelegram("1", "hi");

        server.verify();
    }
}
