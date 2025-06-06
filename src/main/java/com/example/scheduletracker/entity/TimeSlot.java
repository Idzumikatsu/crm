package com.example.scheduletracker.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;

@Entity
@Table(name = "time_slots")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
}
