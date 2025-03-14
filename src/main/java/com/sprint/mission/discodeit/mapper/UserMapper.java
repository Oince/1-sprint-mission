package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.response.BinaryContentResponse;
import com.sprint.mission.discodeit.dto.response.UserResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {

  private final BinaryContentMapper binaryContentMapper;

  public UserResponse toDto(User user) {
    Optional<BinaryContent> optionalContent = user.getProfile();
    BinaryContentResponse profile = null;
    if (optionalContent.isPresent()) {
      profile = binaryContentMapper.toDto(optionalContent.get());
    }
    return UserResponse.of(user, profile);
  }
}
