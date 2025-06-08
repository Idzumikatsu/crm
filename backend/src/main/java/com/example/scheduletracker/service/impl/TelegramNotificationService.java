package com.example.scheduletracker.service.impl;

import com.example.scheduletracker.service.NotificationService;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Primary
@ConditionalOnProperty("telegram.bot-token")
public class TelegramNotificationService implements NotificationService {

  private static final Logger log = LoggerFactory.getLogger(TelegramNotificationService.class);

  private final RestTemplate restTemplate;
  private final String apiUrl;

  @Autowired
  public TelegramNotificationService(@Value("${telegram.bot-token}") String token) {
    this(new RestTemplate(), token);
  }

  TelegramNotificationService(RestTemplate restTemplate, String token) {
    this.restTemplate = restTemplate;
    this.apiUrl = "https://api.telegram.org/bot" + token + "/sendMessage";
  }

  @Override
  public void sendEmail(String to, String subject, String body) {
    log.info("Send email to {} subject {}", to, subject);
  }

  @Override
  public void sendTelegram(String chatId, String text) {
    Map<String, String> payload = Map.of("chat_id", chatId, "text", text);
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    try {
      restTemplate.postForObject(apiUrl, new HttpEntity<>(payload, headers), String.class);
      log.info("Sent telegram to {}", chatId);
    } catch (Exception e) {
      log.error("Failed to send telegram", e);
    }
  }
}
