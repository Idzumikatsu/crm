package com.example.scheduletracker.entity;

import jakarta.persistence.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

/** Weekly availability template for teacher */
@Entity
@Table(name = "availability_template")
public class AvailabilityTemplate {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(optional = false)
  @JoinColumn(name = "teacher_id")
  private Teacher teacher;

  @Column(name = "week_day", nullable = false)
  @Enumerated(EnumType.STRING)
  private DayOfWeek weekDay;

  @Column(name = "start_time", nullable = false)
  private LocalTime startTime;

  @Column(name = "end_time", nullable = false)
  private LocalTime endTime;

  @Column(name = "until_date")
  private LocalDate untilDate;

  public AvailabilityTemplate() {}

  public AvailabilityTemplate(
      Long id,
      Teacher teacher,
      DayOfWeek weekDay,
      LocalTime startTime,
      LocalTime endTime,
      LocalDate untilDate) {
    this.id = id;
    this.teacher = teacher;
    this.weekDay = weekDay;
    this.startTime = startTime;
    this.endTime = endTime;
    this.untilDate = untilDate;
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

  public DayOfWeek getWeekDay() {
    return weekDay;
  }

  public void setWeekDay(DayOfWeek weekDay) {
    this.weekDay = weekDay;
  }

  public LocalTime getStartTime() {
    return startTime;
  }

  public void setStartTime(LocalTime startTime) {
    this.startTime = startTime;
  }

  public LocalTime getEndTime() {
    return endTime;
  }

  public void setEndTime(LocalTime endTime) {
    this.endTime = endTime;
  }

  public LocalDate getUntilDate() {
    return untilDate;
  }

  public void setUntilDate(LocalDate untilDate) {
    this.untilDate = untilDate;
  }
}
