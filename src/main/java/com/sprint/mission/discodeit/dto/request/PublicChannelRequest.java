package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PublicChannelRequest(
    @NotNull @Size(max = 100) String name,
    @NotNull @Size(max = 500) String description
) {

}
