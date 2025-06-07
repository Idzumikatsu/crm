package com.example.scheduletracker.repository;

import com.example.scheduletracker.entity.NotificationTemplate;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationTemplateRepository
    extends JpaRepository<NotificationTemplate, UUID> {
  Optional<NotificationTemplate> findByCodeAndLang(String code, String lang);
}
