package com.example.scheduletracker.entity;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

/** Time slot for teacher availability. */
@Entity
@Table(name = "time_slots")
public class TimeSlot {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(optional = false)
  @JoinColumn(name = "teacher_id")
  private Teacher teacher;

  @Column(nullable = false)
  private OffsetDateTime start;

  @Column(name = "end_time", nullable = false)
  private OffsetDateTime endTime;

  public TimeSlot() {}

  public TimeSlot(Long id, Teacher teacher, OffsetDateTime start, OffsetDateTime endTime) {
    this.id = id;
    this.teacher = teacher;
    this.start = start;
    this.endTime = endTime;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Teacher getTeacher() {
    return teacher;
  }

  public void setTeacher(Teacher teacher) {
    this.teacher = teacher;
  }

  public OffsetDateTime getStart() {
    return start;
  }

  public void setStart(OffsetDateTime start) {
    this.start = start;
  }

  public OffsetDateTime getEndTime() {
    return endTime;
  }

  public void setEndTime(OffsetDateTime endTime) {
    this.endTime = endTime;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {
    private Long id;
    private Teacher teacher;
    private OffsetDateTime start;
    private OffsetDateTime endTime;

    public Builder id(Long id) {
      this.id = id;
      return this;
    }

    public Builder teacher(Teacher teacher) {
      this.teacher = teacher;
      return this;
    }

    public Builder start(OffsetDateTime start) {
      this.start = start;
      return this;
    }

    public Builder endTime(OffsetDateTime endTime) {
      this.endTime = endTime;
      return this;
    }

    public TimeSlot build() {
      return new TimeSlot(id, teacher, start, endTime);
    }
  }
}
