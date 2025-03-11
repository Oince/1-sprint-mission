package com.sprint.mission.discodeit.dto.response;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import java.util.Optional;
import java.util.UUID;

public record UserResponse(
    UUID id,
    String username,
    String email,
    BinaryContentResponse profile,
    boolean online
) {

  public static UserResponse from(User user) {
    Optional<BinaryContent> optionalContent = user.getProfile();
    BinaryContentResponse profile = null;
    if (optionalContent.isPresent()) {
      profile = BinaryContentResponse.from(optionalContent.get());
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
