package com.example.scheduletracker.entity;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

/** Teacher entity. */
@Entity
@Table(name = "teachers")
public class Teacher {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column(length = 1000)
  private String bio;

  @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<TeacherStudent> teacherStudents = new HashSet<>();

  public Teacher() {}

  public Teacher(Long id, String name, String bio) {
    this.id = id;
    this.name = name;
    this.bio = bio;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getBio() {
    return bio;
  }

  public void setBio(String bio) {
    this.bio = bio;
  }

  public Set<TeacherStudent> getTeacherStudents() {
    return teacherStudents;
  }

  public void setTeacherStudents(Set<TeacherStudent> teacherStudents) {
    this.teacherStudents = teacherStudents;
  }
}
