package com.example.scheduletracker.service;

public interface NotificationService {
  void sendEmail(String to, String subject, String body);

  void sendTelegram(String chatId, String text);
}
