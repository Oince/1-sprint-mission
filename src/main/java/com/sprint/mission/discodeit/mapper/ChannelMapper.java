package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.response.ChannelDetailResponse;
import com.sprint.mission.discodeit.dto.response.UserResponse;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Channel.Type;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChannelMapper {

  private final UserRepository userRepository;
  private final MessageRepository messageRepository;
  private final ReadStatusRepository readStatusRepository;
  private final UserMapper userMapper;

  public ChannelDetailResponse toDto(Channel channel) {
    List<Message> messages = messageRepository.findByChannel_Id(channel.getId());
    Instant latestMessageTime = messages.stream()
        .max(Comparator.comparing(Message::getCreatedAt))
        .map(Message::getCreatedAt)
        .orElse(channel.getCreatedAt());

    List<UserResponse> participants;

    if (channel.getType() == Type.PRIVATE) {
      participants = readStatusRepository.findByChannel(channel).stream()
          .map(ReadStatus::getUser)
          .map(UserResponse::from)
          .toList();
    } else {
      participants = userRepository.findAll().stream()
          .map(UserResponse::from)
          .toList();
    }

    return ChannelDetailResponse.of(channel, latestMessageTime, participants);
  }
}
