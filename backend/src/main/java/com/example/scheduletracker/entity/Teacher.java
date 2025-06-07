package com.example.scheduletracker.entity;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.hibernate.annotations.GenericGenerator;

/** Teacher entity. */
@Entity
@Table(name = "teachers")
public class Teacher {
  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  @Column(columnDefinition = "uuid", updatable = false, nullable = false)
  private UUID id;

  @Column(nullable = false)
  private String name;

  @Column(length = 1000)
  private String bio;

  @Column(nullable = false, length = 3)
  private String currency = "RUB";

  @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<TeacherStudent> teacherStudents = new HashSet<>();

  public Teacher() {}

  public Teacher(UUID id, String name, String bio, String currency) {
    this.id = id;
    this.name = name;
    this.bio = bio;
    this.currency = currency;
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
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

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
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
    private UUID id;
    private String name;
    private String bio;
    private String currency = "RUB";

    public Builder id(UUID id) {
      this.id = id;
      return this;
    }

    public Builder name(String name) {
      this.name = name;
      return this;
    }

    public Builder bio(String bio) {
      this.bio = bio;
      return this;
    }

    public Builder currency(String currency) {
      this.currency = currency;
      return this;
    }

    public Teacher build() {
      return new Teacher(id, name, bio, currency);
    }
  }
}
