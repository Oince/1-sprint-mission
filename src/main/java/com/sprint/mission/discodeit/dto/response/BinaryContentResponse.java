package com.sprint.mission.discodeit.dto.response;

import com.sprint.mission.discodeit.entity.BinaryContent;
import java.util.UUID;

public record BinaryContentResponse(
    UUID id,
    Long size,
    String fileName,
    String contentType
) {

  public static BinaryContentResponse from(BinaryContent binaryContent) {
    return new BinaryContentResponse(
        binaryContent.getId(),
        binaryContent.getSize(),
        binaryContent.getFileName(),
        binaryContent.getContentType()
    );
  }
}
