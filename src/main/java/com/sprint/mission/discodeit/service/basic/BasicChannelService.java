package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.PublicChannelRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Channel.Type;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.NotFoundException;
import com.sprint.mission.discodeit.exception.PrivateChannelModificationException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {

  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;
  private final MessageRepository messageRepository;
  private final ReadStatusRepository readStatusRepository;
  private final BinaryContentRepository binaryContentRepository;
  private final BinaryContentStorage binaryContentStorage;

  @Override
  public Channel createPrivateChannel(List<UUID> userIds) {
    List<User> users = userIds.stream()
        .map(userRepository::findById)
        .map(user -> user.orElseThrow(() -> new NotFoundException("등록되지 않은 user.")))
        .toList();
    StringBuilder stringBuilder = new StringBuilder();
    users.stream()
        .map(User::getUsername)
        .forEach(name -> stringBuilder.append(name).append(","));
    stringBuilder.deleteCharAt(stringBuilder.length() - 1);

    Channel channel = channelRepository
        .save(Channel.of(Channel.Type.PRIVATE, stringBuilder.toString(), null));
    users.forEach(user -> readStatusRepository.save(ReadStatus.of(user, channel)));

    return channel;
  }

  @Override
  public Channel createPublicChannel(PublicChannelRequest publicChannelRequest) {
    return channelRepository.save(Channel.of(Channel.Type.PUBLIC, publicChannelRequest.name(),
        publicChannelRequest.description()));
  }

  @Override
  public Channel readChannel(UUID channelId) {
    return channelRepository.findById(channelId)
        .orElseThrow(() -> new NotFoundException("등록되지 않은 channel. id=" + channelId));
  }

  @Override
  public List<Channel> readAllByUserId(UUID userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new NotFoundException("등록되지 않은 user. id=" + userId));

    List<Channel> privateChannels = readStatusRepository.findByUser(user).stream()
        .map(ReadStatus::getChannel)
        .toList();
    List<Channel> publicChannels = channelRepository.findByType(Type.PUBLIC);

    return Stream.concat(privateChannels.stream(), publicChannels.stream()).toList();
  }

  @Override
  public Channel updateChannel(UUID channelId, PublicChannelUpdateRequest updateRequest) {
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> new NotFoundException("등록되지 않은 channel. id=" + channelId));
    if (channel.getType() == Channel.Type.PRIVATE) {
      throw new PrivateChannelModificationException("private 채널은 수정할 수 없습니다.");
    }

    channel.updateName(updateRequest.newName());
    channel.updateDescription(updateRequest.newDescription());
    channelRepository.save(channel);
    return channel;
  }

  @Override
  public void deleteChannel(UUID channelId) {
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> new NotFoundException("등록되지 않은 channel. id=" + channelId));
    List<Message> messages = messageRepository.findByChannel(channel);

    for (Message message : messages) {
      message.getAttachments().stream()
          .map(attachment -> binaryContentRepository.findById(attachment.getId()))
          .filter(Optional::isPresent)
          .map(Optional::get)
          .forEach(attachment -> {
            binaryContentStorage.delete(attachment.getId());
            binaryContentRepository.deleteById(attachment.getId());
          });
      messageRepository.delete(message);
    }

    readStatusRepository.deleteByChannel(channel);
    channelRepository.deleteById(channelId);
  }
}