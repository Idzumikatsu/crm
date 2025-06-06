package com.example.scheduletracker.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

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
  private LocalDateTime start;

  @Column(name = "end_time", nullable = false)
  private LocalDateTime endTime;

  public TimeSlot() {}

  public TimeSlot(Long id, Teacher teacher, LocalDateTime start, LocalDateTime endTime) {
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

  public LocalDateTime getStart() {
    return start;
  }

  public void setStart(LocalDateTime start) {
    this.start = start;
  }

  public LocalDateTime getEndTime() {
    return endTime;
  }

  public void setEndTime(LocalDateTime endTime) {
    this.endTime = endTime;
  }
}
