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
public class Channel implements Serializable {

  private static final long serialVersionUID = 1L;

  private final UUID id;
  private final Instant createdAt;
  private Instant updatedAt;

  private Type type;
  private String name;
  private String description;

  public enum Type {
    PUBLIC, PRIVATE
  }

  public static Channel of(Type type, String name, String description) {
    Instant now = Instant.now();
    return Channel.builder()
        .id(UUID.randomUUID())
        .createdAt(now)
        .updatedAt(now)
        .type(type)
        .name(name)
        .description(description)
        .build();
  }

  public void updateType(Type type) {
    this.type = type;
    updatedAt = Instant.now();
  }

  public void updateName(String name) {
    this.name = name;
    updatedAt = Instant.now();
  }

  public void updateDescription(String description) {
    this.description = description;
    updatedAt = Instant.now();
  }
}