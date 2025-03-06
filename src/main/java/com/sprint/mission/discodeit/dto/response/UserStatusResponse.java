package com.sprint.mission.discodeit.dto.response;

import com.sprint.mission.discodeit.entity.UserStatus;
import java.time.Instant;
import java.util.UUID;

public record UserStatusResponse(
    UUID id,
    UUID userId,
    Instant createdAt,
    Instant updatedAt,
    Instant lastActiveAt,
    boolean online
) {

  public static UserStatusResponse from(UserStatus userStatus) {
    return new UserStatusResponse(
        userStatus.getId(),
        userStatus.getUserId(),
        userStatus.getCreatedAt(),
        userStatus.getUpdateAt(),
        userStatus.getLastActiveAt(),
        userStatus.isOnline()
    );
  }
}
