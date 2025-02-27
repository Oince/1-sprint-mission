package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import lombok.Getter;
import lombok.ToString;


@Getter
@ToString
public class User implements Serializable {

  private static final long serialVersionUID = 1L;

  private final UUID id;
  private final Instant createdAt;
  private Instant updatedAt;

  private String username;
  private String email;
  private String password;
  private UUID profileId;

  private User(String username, String email, String password) {
    this.id = UUID.randomUUID();
    this.createdAt = Instant.now();
    this.updatedAt = createdAt;
    this.email = email;
    this.password = password;
    this.username = username;
  }

  public static User of(String username, String email, String password) {
    return new User(username, email, password);
  }

  public void updateEmail(String email) {
    this.email = email;
    updatedAt = Instant.now();
  }

  public void updatePassword(String password) {
    this.password = password;
    updatedAt = Instant.now();
  }

  public void updateName(String username) {
    this.username = username;
    updatedAt = Instant.now();
  }

  public void updateProfile(UUID profileId) {
    this.profileId = profileId;
    updatedAt = Instant.now();
  }
}
