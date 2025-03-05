package com.sprint.mission.discodeit.dto.response;

import com.sprint.mission.discodeit.entity.UserStatus;
import java.time.Instant;
import java.util.UUID;

public record UserStatusResponse(
    UUID id,
    Instant createdAt,
    Instant updatedAt,
    UUID userId,
    Instant lastActiveAt,
    boolean online
) {

  public static UserStatusResponse from(UserStatus userStatus) {
    return new UserStatusResponse(
        userStatus.getUserId(),
        userStatus.getCreatedAt(),
        userStatus.getUpdateAt(),
        userStatus.getUserId(),
        userStatus.getUpdateAt(),
        userStatus.isOnline()
    );
  }
}
