package com.example.scheduletracker.entity;

import jakarta.persistence.*;
import java.util.UUID;
import org.hibernate.annotations.GenericGenerator;

/** User of the system. */
@Entity
@Table(name = "users")
public class User {
  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  @Column(columnDefinition = "uuid", updatable = false, nullable = false)
  private UUID id;

  @Column(name = "email", unique = true, nullable = false)
  private String username;

  @Column(name = "pwd", nullable = false)
  private String password;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Role role;

  @Column(name = "two_fa_secret")
  private String twoFaSecret;

  @Column(name = "two_fa_enabled")
  private boolean twoFaEnabled;

  @Column(nullable = false)
  private boolean active;

  public User() {}

  public User(
      UUID id,
      String username,
      String password,
      Role role,
      String twoFaSecret,
      boolean twoFaEnabled,
      boolean active) {
    this.id = id;
    this.username = username;
    this.password = password;
    this.role = role;
    this.twoFaSecret = twoFaSecret;
    this.twoFaEnabled = twoFaEnabled;
    this.active = active;
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Role getRole() {
    return role;
  }

  public void setRole(Role role) {
    this.role = role;
  }

  public String getTwoFaSecret() {
    return twoFaSecret;
  }

  public void setTwoFaSecret(String twoFaSecret) {
    this.twoFaSecret = twoFaSecret;
  }

  public boolean isTwoFaEnabled() {
    return twoFaEnabled;
  }

  public void setTwoFaEnabled(boolean twoFaEnabled) {
    this.twoFaEnabled = twoFaEnabled;
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {
    private UUID id;
    private String username;
    private String password;
    private Role role;
    private String twoFaSecret;
    private boolean twoFaEnabled;
    private boolean active;

    public Builder id(UUID id) {
      this.id = id;
      return this;
    }

    public Builder username(String username) {
      this.username = username;
      return this;
    }

    public Builder password(String password) {
      this.password = password;
      return this;
    }

    public Builder role(Role role) {
      this.role = role;
      return this;
    }

    public Builder twoFaSecret(String twoFaSecret) {
      this.twoFaSecret = twoFaSecret;
      return this;
    }

    public Builder twoFaEnabled(boolean twoFaEnabled) {
      this.twoFaEnabled = twoFaEnabled;
      return this;
    }

    public Builder active(boolean active) {
      this.active = active;
      return this;
    }

    public User build() {
      return new User(id, username, password, role, twoFaSecret, twoFaEnabled, active);
    }
  }

  public enum Role {
    ADMIN,
    STUDENT,
    TEACHER,
    MANAGER
  }
}
