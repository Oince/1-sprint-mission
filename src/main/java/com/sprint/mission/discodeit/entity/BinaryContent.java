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

  private final Long size;
  private final String fileName;
  private final String contentType;
  private final String path;

  public static BinaryContent of(UUID id, Long size, String fileName, String contentType,
      String path) {
    Instant now = Instant.now();
    return BinaryContent.builder()
        .id(id)
        .createdAt(now)
        .size(size)
        .fileName(fileName)
        .contentType(contentType)
        .path(path)
        .build();
  }
}
