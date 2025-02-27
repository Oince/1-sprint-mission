package com.sprint.mission.discodeit.dto.request;

import java.util.UUID;

public record MessageCreateRequest(
    UUID writer,
    String content,
    UUID channelId
) {

}
