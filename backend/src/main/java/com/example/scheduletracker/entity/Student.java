package com.example.scheduletracker.entity;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

/** Student entity. */
@Entity
@Table(name = "students")
public class Student {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false, unique = true)
  private String email;

  @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<TeacherStudent> teacherStudents = new HashSet<>();

  public Student() {}

  public Student(Long id, String name, String email) {
    this.id = id;
    this.name = name;
    this.email = email;
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

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Set<TeacherStudent> getTeacherStudents() {
    return teacherStudents;
  }

  public void setTeacherStudents(Set<TeacherStudent> teacherStudents) {
    this.teacherStudents = teacherStudents;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {
    private Long id;
    private String name;
    private String email;

    public Builder id(Long id) {
      this.id = id;
      return this;
    }

    public Builder name(String name) {
      this.name = name;
      return this;
    }

    public Builder email(String email) {
      this.email = email;
      return this;
    }

    public Student build() {
      return new Student(id, name, email);
    }
  }
}
