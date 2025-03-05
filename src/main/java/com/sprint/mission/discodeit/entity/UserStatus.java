package com.sprint.mission.discodeit.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class UserStatus implements Serializable {

  private static final long serialVersionUID = 1L;

  private final UUID userId;
  private final Instant createdAt;
  private Instant updateAt;

  public static UserStatus from(UUID userId) {
    Instant now = Instant.now();
    return UserStatus.builder()
        .userId(userId)
        .createdAt(now)
        .updateAt(now)
        .build();
  }

  public boolean isOnline() {
    long minutes = Duration.between(updateAt, Instant.now()).toMinutes();
    return minutes <= 5;
  }

  public void updateLastActiveAt(Instant lastActiveAt) {
    updateAt = lastActiveAt;
  }
}
