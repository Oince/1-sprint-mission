package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.PublicChannelRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Channel.Type;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.exception.ModificationNowAllowedException;
import com.sprint.mission.discodeit.exception.NotFoundException;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {

  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;
  private final MessageRepository messageRepository;
  private final ReadStatusRepository readStatusRepository;
  private final BinaryContentStorage binaryContentStorage;

  @Transactional
  @Override
  public Channel createPrivateChannel(List<UUID> userIds) {
    Channel channel = channelRepository.save(Channel.create(Channel.Type.PRIVATE, null, null));
    userIds.stream()
        .map(userRepository::findById)
        .map(user -> user.orElseThrow(() -> new NotFoundException("등록되지 않은 user.")))
        .forEach(user -> readStatusRepository.save(ReadStatus.create(user, channel)));
    return channel;
  }

  @Transactional
  @Override
  public Channel createPublicChannel(PublicChannelRequest publicChannelRequest) {
    return channelRepository.save(Channel.create(Channel.Type.PUBLIC, publicChannelRequest.name(),
        publicChannelRequest.description()));
  }

  @Override
  public Channel readChannel(UUID channelId) {
    return channelRepository.findById(channelId)
        .orElseThrow(() -> new NotFoundException("등록되지 않은 channel. id=" + channelId));
  }

  @Override
  public List<Channel> readAllByUserId(UUID userId) {
    if (!userRepository.existsById(userId)) {
      throw new NotFoundException("등록되지 않은 user. id=" + userId);
    }

    List<UUID> subscribedChannelIds = readStatusRepository.findByUser_Id(userId).stream()
        .map(ReadStatus::getChannel)
        .map(Channel::getId)
        .toList();

    return channelRepository.findAll().stream()
        .filter(channel -> channel.getType() == Type.PUBLIC ||
            subscribedChannelIds.contains(channel.getId()))
        .toList();
  }

  @Transactional
  @Override
  public Channel updateChannel(UUID channelId, PublicChannelUpdateRequest updateRequest) {
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> new NotFoundException("등록되지 않은 channel. id=" + channelId));
    if (channel.getType() == Channel.Type.PRIVATE) {
      throw new ModificationNowAllowedException("private 채널은 수정할 수 없습니다.");
    }

    channel.updateName(updateRequest.newName());
    channel.updateDescription(updateRequest.newDescription());
    return channelRepository.save(channel);
  }

  @Transactional
  @Override
  public void deleteChannel(UUID channelId) {
    Optional<Channel> optionalChannel = channelRepository.findById(channelId);
    if (optionalChannel.isEmpty()) {
      return;
    }
    Channel channel = optionalChannel.get();
    List<Message> messages = messageRepository.findByChannel_Id(channelId);

    for (Message message : messages) {
      message.getAttachments()
          .forEach(attachment -> binaryContentStorage.delete(attachment.getId()));
      messageRepository.delete(message);
    }

    readStatusRepository.deleteByChannel(channel);
    channelRepository.delete(channel);
  }
}