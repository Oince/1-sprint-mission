package com.sprint.mission.discodeit.dto.response;

import com.sprint.mission.discodeit.entity.BinaryContent;
import java.time.Instant;
import java.util.UUID;

public record BinaryContentResponse(
    UUID id,
    Instant createdAt,
    Long size,
    String fileName,
    String contentType,
    byte[] bytes
) {

  public static BinaryContentResponse of(BinaryContent binaryContent, byte[] bytes) {
    return new BinaryContentResponse(
        binaryContent.getId(),
        binaryContent.getCreatedAt(),
        binaryContent.getSize(),
        binaryContent.getFileName(),
        binaryContent.getContentType(),
        bytes
    );
  }
}
