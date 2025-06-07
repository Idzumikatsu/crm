package com.example.scheduletracker.entity;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;
import org.hibernate.annotations.GenericGenerator;

/** Time slot for teacher availability. */
@Entity
@Table(name = "time_slot")
public class TimeSlot {
  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  @Column(columnDefinition = "uuid", updatable = false, nullable = false)
  private UUID id;

  @ManyToOne(optional = false)
  @JoinColumn(name = "teacher_id")
  private Teacher teacher;

  @Column(name = "start_ts", nullable = false)
  private OffsetDateTime startTs;

  @Column(name = "end_ts", nullable = false)
  private OffsetDateTime endTs;

  public TimeSlot() {}

  public TimeSlot(UUID id, Teacher teacher, OffsetDateTime startTs, OffsetDateTime endTs) {
    this.id = id;
    this.teacher = teacher;
    this.startTs = startTs;
    this.endTs = endTs;
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public Teacher getTeacher() {
    return teacher;
  }

  public void setTeacher(Teacher teacher) {
    this.teacher = teacher;
  }

  public OffsetDateTime getStartTs() {
    return startTs;
  }

  public void setStartTs(OffsetDateTime startTs) {
    this.startTs = startTs;
  }

  public OffsetDateTime getEndTs() {
    return endTs;
  }

  public void setEndTs(OffsetDateTime endTs) {
    this.endTs = endTs;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {
    private UUID id;
    private Teacher teacher;
    private OffsetDateTime startTs;
    private OffsetDateTime endTs;

    public Builder id(UUID id) {
      this.id = id;
      return this;
    }

    public Builder teacher(Teacher teacher) {
      this.teacher = teacher;
      return this;
    }

    public Builder startTs(OffsetDateTime startTs) {
      this.startTs = startTs;
      return this;
    }

    public Builder endTs(OffsetDateTime endTs) {
      this.endTs = endTs;
      return this;
    }

    public TimeSlot build() {
      return new TimeSlot(id, teacher, startTs, endTs);
    }
  }
}
