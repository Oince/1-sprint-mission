package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PublicChannelUpdateRequest(
    @NotNull @Size(max = 100) String newName,
    @NotNull @Size(max = 500) String newDescription
) {

}
