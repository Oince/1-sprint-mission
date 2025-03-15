package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.response.BinaryContentResponse;
import com.sprint.mission.discodeit.dto.response.UserResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import java.util.Optional;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", uses = {BinaryContentMapper.class})
public abstract class UserMapper {

  @Autowired
  protected BinaryContentMapper binaryContentMapper;

  @Mapping(source = "profile", target = "profile")
  @Mapping(source = "status.online", target = "online")
  public abstract UserResponse toDto(User user);

  protected BinaryContentResponse map(Optional<BinaryContent> optionalProfile) {
    return optionalProfile.map(binaryContentMapper::toDto).orElse(null);
  }
}
