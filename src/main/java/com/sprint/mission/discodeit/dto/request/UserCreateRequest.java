package com.sprint.mission.discodeit.dto.request;

import java.util.UUID;

public record UserCreateRequest(
    String email,
    String password,
    String username,
    UUID id
) {

}
