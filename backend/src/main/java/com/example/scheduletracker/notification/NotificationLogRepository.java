package com.example.scheduletracker.notification;

import java.time.OffsetDateTime;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationLogRepository extends JpaRepository<NotificationLog, Long> {
  void deleteBySentAtBefore(OffsetDateTime ts);
}
