package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;


@Getter
@ToString
@Builder(access = AccessLevel.PRIVATE)
public class User implements Serializable {

  private static final long serialVersionUID = 1L;

  private final UUID id;
  private final Instant createdAt;
  private Instant updatedAt;

  private String username;
  private String email;
  private String password;
  private UUID profileId;

  public static User of(String username, String email, String password) {
    Instant now = Instant.now();
    return User.builder()
        .id(UUID.randomUUID())
        .createdAt(now)
        .updatedAt(now)
        .username(username)
        .email(email)
        .password(password)
        .build();
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
