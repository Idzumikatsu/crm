package com.example.scheduletracker.entity;

import jakarta.persistence.*;

/** User of the system. */
@Entity
@Table(name = "users")
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true, nullable = false)
  private String username;

  @Column(nullable = false)
  private String password;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Role role;

  @Column(name = "two_fa_secret")
  private String twoFaSecret;

  public User() {}

  public User(Long id, String username, String password, Role role, String twoFaSecret) {
    this.id = id;
    this.username = username;
    this.password = password;
    this.role = role;
    this.twoFaSecret = twoFaSecret;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
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

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {
    private Long id;
    private String username;
    private String password;
    private Role role;
    private String twoFaSecret;

    public Builder id(Long id) {
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

    public User build() {
      return new User(id, username, password, role, twoFaSecret);
    }
  }

  public enum Role {
    ADMIN,
    STUDENT,
    TEACHER,
    MANAGER
  }
}
