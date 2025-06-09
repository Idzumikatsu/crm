package com.example.scheduletracker.service.impl;

import com.example.scheduletracker.service.NotificationService;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class LoggingNotificationService implements NotificationService {
  private static final Logger log = LoggerFactory.getLogger(LoggingNotificationService.class);

  private final JavaMailSender mailSender;
  private final String from;

  @Autowired
  public LoggingNotificationService(JavaMailSender mailSender,
      @Value("${app.mail.from:no-reply@example.com}") String from) {
    this.mailSender = mailSender;
    this.from = from;
  }


  @Async
  @Override
  public void sendEmail(String to, String subject, String body) {
    try {
      MimeMessage message = mailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");
      helper.setFrom(from);
      helper.setTo(to);
      helper.setSubject(subject);
      helper.setText(body, true);
      mailSender.send(message);
      log.info("Sent email to {} subject {}", to, subject);
    } catch (Exception e) {
      log.error("Failed to send email", e);
    }
  }

  @Override
  public void sendTelegram(String chatId, String text) {
    log.info("Send telegram to {}: {}", chatId, text);
  }
}
