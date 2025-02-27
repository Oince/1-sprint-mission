package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.ToString;


@Getter
@ToString
public class Message implements Serializable {

  private static final long serialVersionUID = 1L;

  private final UUID id;
  private final Instant createdAt;
  private Instant updatedAt;

  private final User writer;
  private final Channel channel;
  private final List<UUID> attachmentIds;
  private String content;

  private Message(User writer, String content, Channel channel, List<UUID> attachmentIds) {
    this.id = UUID.randomUUID();
    this.createdAt = Instant.now();
    this.updatedAt = createdAt;
    this.writer = writer;
    this.content = content;
    this.channel = channel;
    this.attachmentIds = attachmentIds;
  }

  public static Message of(User writer, String content, Channel channel, List<UUID> attachmentIds) {
    return new Message(writer, content, channel, attachmentIds);
  }

  public void updateContent(String content) {
    this.content = content;
    updatedAt = Instant.now();
  }
}
