package com.sprint.mission.discodeit.dto.request;

import java.util.List;
import java.util.UUID;

public record PrivateChannelRequest(
    List<UUID> participantIds
) {

}
