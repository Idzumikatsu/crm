package com.example.scheduletracker.service.impl;

import com.example.scheduletracker.audit.AuditLogRepository;
import com.example.scheduletracker.notification.NotificationLogRepository;
import java.time.OffsetDateTime;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class DataRetentionService {
  private final AuditLogRepository auditRepo;
  private final NotificationLogRepository notificationRepo;

  public DataRetentionService(
      AuditLogRepository auditRepo, NotificationLogRepository notificationRepo) {
    this.auditRepo = auditRepo;
    this.notificationRepo = notificationRepo;
  }

  @Scheduled(cron = "0 30 2 * * ?")
  public void purgeOldEntries() {
    OffsetDateTime now = OffsetDateTime.now();
    auditRepo.deleteByTsBefore(now.minusYears(3));
    notificationRepo.deleteBySentAtBefore(now.minusYears(1));
  }
}
