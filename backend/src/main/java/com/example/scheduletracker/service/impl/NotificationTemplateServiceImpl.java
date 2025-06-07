package com.example.scheduletracker.service.impl;

import com.example.scheduletracker.entity.NotificationTemplate;
import com.example.scheduletracker.repository.NotificationTemplateRepository;
import com.example.scheduletracker.service.NotificationTemplateService;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class NotificationTemplateServiceImpl implements NotificationTemplateService {
  private final NotificationTemplateRepository repo;

  public NotificationTemplateServiceImpl(NotificationTemplateRepository repo) {
    this.repo = repo;
  }

  @Override
  public NotificationTemplate save(NotificationTemplate template) {
    return repo.save(template);
  }

  @Override
  public List<NotificationTemplate> findAll() {
    return repo.findAll();
  }

  @Override
  public Optional<NotificationTemplate> findById(UUID id) {
    return repo.findById(id);
  }

  @Override
  public void deleteById(UUID id) {
    repo.deleteById(id);
  }

  @Override
  public Optional<NotificationTemplate> findByCodeAndLang(String code, String lang) {
    return repo.findByCodeAndLang(code, lang);
  }
}
