package com.example.scheduletracker.entity;

import jakarta.persistence.*;
import java.util.UUID;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "notification_template")
public class NotificationTemplate {
  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  @Column(columnDefinition = "uuid", updatable = false, nullable = false)
  private UUID id;

  @Column(nullable = false)
  private String code;

  @Column(nullable = false)
  private String lang;

  private String subject;

  @Lob
  @Column(name = "body_html")
  private String bodyHtml;

  public NotificationTemplate() {}

  public NotificationTemplate(UUID id, String code, String lang, String subject, String bodyHtml) {
    this.id = id;
    this.code = code;
    this.lang = lang;
    this.subject = subject;
    this.bodyHtml = bodyHtml;
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getLang() {
    return lang;
  }

  public void setLang(String lang) {
    this.lang = lang;
  }

  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public String getBodyHtml() {
    return bodyHtml;
  }

  public void setBodyHtml(String bodyHtml) {
    this.bodyHtml = bodyHtml;
  }
}
