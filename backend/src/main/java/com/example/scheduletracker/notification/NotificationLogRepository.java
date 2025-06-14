package com.example.scheduletracker.notification;

import java.time.OffsetDateTime;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationLogRepository extends JpaRepository<NotificationLog, UUID> {
  void deleteBySentAtBefore(OffsetDateTime ts);
}
