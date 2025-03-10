package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "channels")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Channel extends BaseUpdatableEntity {

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private Type type;

  private String name;
  private String description;

  public enum Type {
    PUBLIC, PRIVATE
  }

  @Builder(access = AccessLevel.PRIVATE)
  private Channel(Type type, String name, String description) {
    this.type = type;
    this.name = name;
    this.description = description;
  }

  public static Channel of(Type type, String name, String description) {
    return Channel.builder()
        .type(type)
        .name(name)
        .description(description)
        .build();
  }

  public void updateType(Type type) {
    this.type = type;
  }

  public void updateName(String name) {
    this.name = name;
  }

  public void updateDescription(String description) {
    this.description = description;
  }
}