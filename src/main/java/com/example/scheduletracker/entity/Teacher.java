// Teacher.java
package com.example.scheduletracker.entity;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import lombok.*;

@Entity
@Table(name = "teachers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Teacher {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column(length = 1000)
  private String bio;

  @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL, orphanRemoval = true)
  @Builder.Default
  private Set<TeacherStudent> teacherStudents = new HashSet<>();
}
