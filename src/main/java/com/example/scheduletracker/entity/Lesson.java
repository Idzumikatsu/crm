package com.example.scheduletracker.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/** Lesson entity. */
@Entity
@Table(name = "lessons")
public class Lesson {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private LocalDateTime dateTime;

  @Column(nullable = false)
  private Integer duration; // minutes

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Status status = Status.SCHEDULED;

  @ManyToOne(optional = false)
  @JoinColumn(name = "teacher_id")
  private Teacher teacher;

  @ManyToOne(optional = false)
  @JoinColumn(name = "group_id")
  private Group group;

  public Lesson() {}

  public Lesson(Long id, LocalDateTime dateTime, Integer duration, Status status, Teacher teacher, Group group) {
    this.id = id;
    this.dateTime = dateTime;
    this.duration = duration;
    this.status = status;
    this.teacher = teacher;
    this.group = group;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public LocalDateTime getDateTime() {
    return dateTime;
  }

  public void setDateTime(LocalDateTime dateTime) {
    this.dateTime = dateTime;
  }

  public Integer getDuration() {
    return duration;
  }

  public void setDuration(Integer duration) {
    this.duration = duration;
  }

  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = status;
  }

  public Teacher getTeacher() {
    return teacher;
  }

  public void setTeacher(Teacher teacher) {
    this.teacher = teacher;
  }

  public Group getGroup() {
    return group;
  }

  public void setGroup(Group group) {
    this.group = group;
  }

  public enum Status {
    SCHEDULED,
    CONFIRMED,
    CANCELED
  }
}
