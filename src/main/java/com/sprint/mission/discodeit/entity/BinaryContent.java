package com.sprint.mission.discodeit.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class BinaryContent implements Serializable {

  private static final long serialVersionUID = 1L;

  private final UUID id;
  private final Instant createdAt;

  private final String name;
  private final String path;

  public static BinaryContent of(UUID id, String name, String path) {
    return BinaryContent.builder()
        .id(id)
        .name(name)
        .path(path)
        .build();
  }
}
