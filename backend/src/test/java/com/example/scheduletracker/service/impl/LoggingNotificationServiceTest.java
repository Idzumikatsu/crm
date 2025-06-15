package com.example.scheduletracker.service.impl;

import static org.mockito.Mockito.*;

import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.javamail.JavaMailSender;

class LoggingNotificationServiceTest {

  @Mock private JavaMailSender mailSender;
  @Mock private MimeMessage message;

  private LoggingNotificationService service;

  @BeforeEach
  void setup() {
    MockitoAnnotations.openMocks(this);
    when(mailSender.createMimeMessage()).thenReturn(message);
    service = new LoggingNotificationService(mailSender, "from@example.com", "smtp");
  }

  @Test
  void sendEmailUsesMailSender() {
    service.sendEmail("to@example.com", "sub", "body");
    verify(mailSender).send(message);
  }

  @Test
  void sendEmailSkippedWhenHostMissing() {
    service = new LoggingNotificationService(mailSender, "from@example.com", "");
    service.sendEmail("to@example.com", "sub", "body");
    verifyNoInteractions(mailSender);
  }
}
