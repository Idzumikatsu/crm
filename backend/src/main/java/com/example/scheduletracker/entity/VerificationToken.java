package com.example.scheduletracker.entity;

import jakarta.persistence.*;
import java.util.UUID;

/** Token used for account verification. */
@Entity
@Table(name = "verification_token")
public class VerificationToken {

  @Id
  @Column(columnDefinition = "uuid")
  private UUID token;

  @ManyToOne(optional = false)
  @JoinColumn(name = "user_id")
  private User user;

  public VerificationToken() {}

  public VerificationToken(UUID token, User user) {
    this.token = token;
    this.user = user;
  }

  public UUID getToken() {
    return token;
  }

  public void setToken(UUID token) {
    this.token = token;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }
}
