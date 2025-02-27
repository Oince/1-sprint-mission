package com.sprint.mission.discodeit.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class ReadStatus implements Serializable {

  private static final long serialVersionUID = 1L;

  private final UUID id;
  private final Instant createdAt;
  private Instant updateAt;

  private UUID userId;
  private UUID channelId;

  public static ReadStatus of(UUID userId, UUID channelId) {
    Instant now = Instant.now();
    return ReadStatus.builder()
        .id(UUID.randomUUID())
        .createdAt(now)
        .updateAt(now)
        .userId(userId)
        .channelId(channelId)
        .build();
  }

  public void update() {
    this.updateAt = Instant.now();
  }
}
