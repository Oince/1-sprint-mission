package com.sprint.mission.discodeit.dto.response;

import com.sprint.mission.discodeit.entity.Message;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record MessageResponse(
    UUID id,
    Instant createdAt,
    Instant updatedAt,
    String content,
    UserResponse author,
    UUID channelId,
    List<BinaryContentResponse> attachments
) {

  public static MessageResponse of(Message message, UserResponse userResponse,
      List<BinaryContentResponse> attachments) {
    return new MessageResponse(
        message.getId(),
        message.getCreatedAt(),
        message.getUpdatedAt(),
        message.getContent(),
        userResponse,
        message.getChannel().getId(),
        attachments
    );
  }
}
