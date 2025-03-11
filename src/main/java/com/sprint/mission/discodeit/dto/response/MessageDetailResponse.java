package com.sprint.mission.discodeit.dto.response;

import com.sprint.mission.discodeit.entity.Message;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record MessageDetailResponse(
    UUID id,
    Instant createdAt,
    Instant updatedAt,
    String content,
    UUID authorId,
    UUID channelId,
    List<BinaryContentResponse> attachments
) {

  public static MessageDetailResponse from(Message message) {
    List<BinaryContentResponse> attachments = message.getAttachments().stream()
        .map(BinaryContentResponse::from)
        .toList();
    return new MessageDetailResponse(
        message.getId(),
        message.getCreatedAt(),
        message.getUpdatedAt(),
        message.getContent(),
        message.getAuthor().getId(),
        message.getChannel().getId(),
        attachments
    );
  }
}
