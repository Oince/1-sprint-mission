package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.response.ReadStatusResponse;
import com.sprint.mission.discodeit.entity.ReadStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReadStatusMapper {

  @Mapping(source = "user.id", target = "userId")
  @Mapping(source = "channel.id", target = "channelId")
  ReadStatusResponse toDto(ReadStatus readStatus);
}
