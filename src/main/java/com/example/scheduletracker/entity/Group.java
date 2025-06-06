// Group.java
package com.example.scheduletracker.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "groups")
public class Group {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  private String description;

  public Group() {}

  public Group(Long id, String name, String description) {
    this.id = id;
    this.name = name;
    this.description = description;
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

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {
    private Long id;
    private String name;
    private String description;

    public Builder id(Long id) {
      this.id = id;
      return this;
    }

    public Builder name(String name) {
      this.name = name;
      return this;
    }

    public Builder description(String description) {
      this.description = description;
      return this;
    }

    public Group build() {
      return new Group(id, name, description);
    }
  }
}
