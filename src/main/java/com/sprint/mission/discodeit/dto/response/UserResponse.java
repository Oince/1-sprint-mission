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

  public static UserResponse from(User user) {
    BinaryContentResponse profile = null;
    if (user.getProfile() != null) {
      profile = BinaryContentResponse.from(user.getProfile());
    }
    return new UserResponse(
        user.getId(),
        user.getUsername(),
        user.getEmail(),
        profile,
        user.getStatus().isOnline()
    );
  }
}
