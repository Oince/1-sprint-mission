package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.response.BinaryContentResponse;
import com.sprint.mission.discodeit.dto.response.MessageResponse;
import com.sprint.mission.discodeit.dto.response.UserResponse;
import com.sprint.mission.discodeit.entity.Message;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageMapper {

  private final BinaryContentMapper binaryContentMapper;
  private final UserMapper userMapper;

  public MessageResponse toDto(Message message) {
    List<BinaryContentResponse> attachments = message.getAttachments().stream()
        .map(binaryContentMapper::toDto)
        .toList();
    UserResponse userResponse = userMapper.toDto(message.getAuthor());
    return MessageResponse.of(message, userResponse, attachments);
  }
}
