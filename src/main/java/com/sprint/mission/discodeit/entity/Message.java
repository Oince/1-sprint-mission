package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;


@Getter
@ToString
@Builder(access = AccessLevel.PRIVATE)
public class Message implements Serializable {

  private static final long serialVersionUID = 1L;

  private final UUID id;
  private final Instant createdAt;
  private Instant updatedAt;

  private final User writer;
  private final Channel channel;
  private final List<UUID> attachmentIds;
  private String content;

  public static Message of(User writer, String content, Channel channel, List<UUID> attachmentIds) {
    Instant now = Instant.now();
    return Message.builder()
        .id(UUID.randomUUID())
        .createdAt(now)
        .updatedAt(now)
        .writer(writer)
        .content(content)
        .channel(channel)
        .attachmentIds(attachmentIds)
        .build();
  }

  public void updateContent(String content) {
    this.content = content;
    updatedAt = Instant.now();
  }
}
