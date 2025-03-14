package com.sprint.mission.discodeit.dto.response;

import com.sprint.mission.discodeit.entity.User;
import java.util.UUID;

public record UserResponse(
    UUID id,
    String username,
    String email,
    BinaryContentResponse profile,
    boolean online
) {

  public static UserResponse of(User user, BinaryContentResponse profile) {
    return new UserResponse(
        user.getId(),
        user.getUsername(),
        user.getEmail(),
        profile,
        user.getStatus().isOnline()
    );
  }

}
