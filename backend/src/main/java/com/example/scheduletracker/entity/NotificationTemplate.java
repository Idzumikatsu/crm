package com.example.scheduletracker.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "notification_template")
public class NotificationTemplate {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String code;

  @Column(nullable = false)
  private String lang;

  private String subject;

  @Lob
  @Column(name = "body_html")
  private String bodyHtml;

  public NotificationTemplate() {}

  public NotificationTemplate(Long id, String code, String lang, String subject, String bodyHtml) {
    this.id = id;
    this.code = code;
    this.lang = lang;
    this.subject = subject;
    this.bodyHtml = bodyHtml;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
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
