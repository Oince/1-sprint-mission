package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class BinaryContent implements Serializable {

  private static final long serialVersionUID = 1L;

  private final UUID id;
  private final Instant createdAt;

  private final String name;
  private final String path;

  private BinaryContent(UUID id, String name, String path) {
    this.id = id;
    this.createdAt = Instant.now();
    this.name = name;
    this.path = path;
  }

  public static BinaryContent of(UUID id, String name, String path) {
    return new BinaryContent(id, name, path);
  }
}
