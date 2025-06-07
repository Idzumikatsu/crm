package com.example.scheduletracker.entity;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.hibernate.annotations.GenericGenerator;

/** Student entity. */
@Entity
@Table(name = "students")
public class Student {
  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  @Column(columnDefinition = "uuid", updatable = false, nullable = false)
  private UUID id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(name = "telegram_username")
  private String telegramUsername;

  @Column(name = "valid_contact", nullable = false)
  private boolean validContact;

  @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<TeacherStudent> teacherStudents = new HashSet<>();

  public Student() {}

  public Student(UUID id, String name, String email) {
    this(id, name, email, null, false);
  }

  public Student(UUID id, String name, String email, String telegramUsername, boolean validContact) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.telegramUsername = telegramUsername;
    this.validContact = validContact;
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

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getTelegramUsername() {
    return telegramUsername;
  }

  public void setTelegramUsername(String telegramUsername) {
    this.telegramUsername = telegramUsername;
  }

  public boolean isValidContact() {
    return validContact;
  }

  public void setValidContact(boolean validContact) {
    this.validContact = validContact;
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
    private String email;
    private String telegramUsername;
    private boolean validContact;

    public Builder id(UUID id) {
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

    public Builder telegramUsername(String telegramUsername) {
      this.telegramUsername = telegramUsername;
      return this;
    }

    public Builder validContact(boolean validContact) {
      this.validContact = validContact;
      return this;
    }

    public Student build() {
      return new Student(id, name, email, telegramUsername, validContact);
    }
  }
}
