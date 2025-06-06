package com.example.scheduletracker.notification;

import com.example.scheduletracker.entity.Lesson;
import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "notification_log")
public class NotificationLog {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(optional = false)
  @JoinColumn(name = "lesson_id")
  private Lesson lesson;

  @Column(nullable = false)
  private String channel;

  private OffsetDateTime sentAt;
  private String status;
  private OffsetDateTime readAt;

  @Lob private String payloadJson;

  public NotificationLog() {}

  public NotificationLog(Lesson lesson, String channel, String status) {
    this.lesson = lesson;
    this.channel = channel;
    this.status = status;
  }

  public Long getId() {
    return id;
  }

  public Lesson getLesson() {
    return lesson;
  }

  public void setLesson(Lesson lesson) {
    this.lesson = lesson;
  }

  public String getChannel() {
    return channel;
  }

  public void setChannel(String channel) {
    this.channel = channel;
  }

  public OffsetDateTime getSentAt() {
    return sentAt;
  }

  public void setSentAt(OffsetDateTime sentAt) {
    this.sentAt = sentAt;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public OffsetDateTime getReadAt() {
    return readAt;
  }

  public void setReadAt(OffsetDateTime readAt) {
    this.readAt = readAt;
  }

  public String getPayloadJson() {
    return payloadJson;
  }

  public void setPayloadJson(String payloadJson) {
    this.payloadJson = payloadJson;
  }
}
