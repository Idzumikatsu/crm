package com.example.scheduletracker.audit;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "audit_log")
public class AuditLog {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String entity;
  private UUID entityId;
  private String action;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(columnDefinition = "jsonb")
  private String oldJson;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(columnDefinition = "jsonb")
  private String newJson;

  private UUID actorId;
  private OffsetDateTime ts = OffsetDateTime.now();

  // getters/setters omitted for brevity
  public AuditLog() {}

  public AuditLog(
      String entity, UUID entityId, String action, String oldJson, String newJson, UUID actorId) {
    this.entity = entity;
    this.entityId = entityId;
    this.action = action;
    this.oldJson = oldJson;
    this.newJson = newJson;
    this.actorId = actorId;
  }
}
