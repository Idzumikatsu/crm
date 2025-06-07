package com.example.scheduletracker.repository;

import com.example.scheduletracker.entity.NotificationTemplate;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationTemplateRepository extends JpaRepository<NotificationTemplate, Long> {
  Optional<NotificationTemplate> findByCodeAndLang(String code, String lang);
}
