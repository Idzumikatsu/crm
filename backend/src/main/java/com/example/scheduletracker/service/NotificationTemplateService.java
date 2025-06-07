package com.example.scheduletracker.service;

import com.example.scheduletracker.entity.NotificationTemplate;
import java.util.List;
import java.util.Optional;

public interface NotificationTemplateService {
  NotificationTemplate save(NotificationTemplate template);

  List<NotificationTemplate> findAll();

  Optional<NotificationTemplate> findById(Long id);

  void deleteById(Long id);

  Optional<NotificationTemplate> findByCodeAndLang(String code, String lang);
}
