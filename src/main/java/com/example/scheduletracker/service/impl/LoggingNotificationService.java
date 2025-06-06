package com.example.scheduletracker.service.impl;

import com.example.scheduletracker.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class LoggingNotificationService implements NotificationService {
    private static final Logger log = LoggerFactory.getLogger(LoggingNotificationService.class);

    @Override
    public void sendEmail(String to, String subject, String body) {
        log.info("Send email to {} subject {}", to, subject);
    }

    @Override
    public void sendTelegram(String chatId, String text) {
        log.info("Send telegram to {}: {}", chatId, text);
    }
}
