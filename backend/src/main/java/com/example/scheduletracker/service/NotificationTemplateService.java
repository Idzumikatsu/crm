package com.example.scheduletracker.service;

import com.example.scheduletracker.entity.NotificationTemplate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NotificationTemplateService {
  NotificationTemplate save(NotificationTemplate template);

  List<NotificationTemplate> findAll();

  Optional<NotificationTemplate> findById(UUID id);

  void deleteById(UUID id);

  Optional<NotificationTemplate> findByCodeAndLang(String code, String lang);
}
