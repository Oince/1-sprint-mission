package com.sprint.mission.discodeit.dto.response;

import java.util.UUID;

public record BinaryContentResponse(
    UUID id,
    Long size,
    String fileName,
    String contentType
) {

}
