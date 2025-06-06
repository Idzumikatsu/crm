package com.example.scheduletracker.audit;

import java.time.OffsetDateTime;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
  void deleteByTsBefore(OffsetDateTime ts);
}
