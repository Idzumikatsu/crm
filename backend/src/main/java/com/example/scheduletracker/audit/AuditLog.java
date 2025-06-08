package com.example.scheduletracker.audit;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "audit_log")
public class AuditLog {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String entity;
  private UUID entityId;
  private String action;

  @Lob private String oldJson;
  @Lob private String newJson;

  private Long actorId;
  private OffsetDateTime ts = OffsetDateTime.now();

  // getters/setters omitted for brevity
  public AuditLog() {}

  public AuditLog(
      String entity, UUID entityId, String action, String oldJson, String newJson, Long actorId) {
    this.entity = entity;
    this.entityId = entityId;
    this.action = action;
    this.oldJson = oldJson;
    this.newJson = newJson;
    this.actorId = actorId;
  }
}
