package com.example.scheduletracker.entity;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "teacher_settings")
public class TeacherSettings {
  @Id
  @Column(name = "teacher_id", columnDefinition = "uuid")
  private UUID teacherId;

  @Column(name = "buffer_min")
  private Integer bufferMin;

  @Column(name = "template")
  private String template;

  public TeacherSettings() {}

  public TeacherSettings(UUID teacherId, Integer bufferMin, String template) {
    this.teacherId = teacherId;
    this.bufferMin = bufferMin;
    this.template = template;
  }

  public UUID getTeacherId() {
    return teacherId;
  }

  public void setTeacherId(UUID teacherId) {
    this.teacherId = teacherId;
  }

  public Integer getBufferMin() {
    return bufferMin;
  }

  public void setBufferMin(Integer bufferMin) {
    this.bufferMin = bufferMin;
  }

  public String getTemplate() {
    return template;
  }

  public void setTemplate(String template) {
    this.template = template;
  }
}
