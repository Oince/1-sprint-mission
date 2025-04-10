package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.response.MessageResponse;
import com.sprint.mission.discodeit.entity.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {BinaryContentMapper.class, UserMapper.class})
public interface MessageMapper {

  @Mapping(source = "channel.id", target = "channelId")
  MessageResponse toDto(Message message);

}
