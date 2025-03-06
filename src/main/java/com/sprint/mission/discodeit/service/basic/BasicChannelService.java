package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.PublicChannelRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.response.ChannelDetailResponse;
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
import com.sprint.mission.discodeit.repository.file.FileManager;
import com.sprint.mission.discodeit.service.ChannelService;
import java.nio.file.Path;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
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
  private final FileManager fileManager;

  @Override
  public Channel createPrivateChannel(List<UUID> userIds) {
    List<User> users = userIds.stream()
        .map(userRepository::findById)
        .map(user -> user.orElseThrow(() -> new NotFoundException("등록되지 않은 user.")))
        .toList();

    Channel channel = Channel.of(Channel.Type.PRIVATE, users.get(0).getUsername(),
        users.get(0).getUsername() + "의 Private 채널");

    users.forEach(user -> readStatusRepository.save(ReadStatus.of(user.getId(), channel.getId())));

    return channelRepository.save(channel);
  }

  @Override
  public Channel createPublicChannel(PublicChannelRequest publicChannelRequest) {
    return channelRepository.save(Channel.of(Channel.Type.PUBLIC, publicChannelRequest.name(),
        publicChannelRequest.description()));
  }

  @Override
  public ChannelDetailResponse readChannel(UUID channelId) {
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> new NotFoundException("등록되지 않은 channel. id=" + channelId));
    List<Message> messages = messageRepository.findByChannelId(channelId);
    List<UUID> participantIds = readStatusRepository.findByChannelId(channelId).stream()
        .map(ReadStatus::getUserId)
        .toList();
    Instant latestMessageTime = messages.stream()
        .max(Comparator.comparing(Message::getCreatedAt))
        .map(Message::getCreatedAt)
        .orElse(channel.getCreatedAt());

    return ChannelDetailResponse.of(channel, latestMessageTime, participantIds);
  }

  @Override
  public List<ChannelDetailResponse> readAllByUserId(UUID userId) {

    List<UUID> privateChannelIds = readStatusRepository.findByUserId(userId).stream()
        .map(ReadStatus::getChannelId)
        .toList();
    List<Channel> channels = channelRepository.findAll().stream()
        .filter(channel -> channel.getType().equals(Type.PUBLIC) || privateChannelIds.contains(
            channel.getId()))
        .toList();
    List<ChannelDetailResponse> channelDetailResponses = new ArrayList<>(100);

    for (Channel channel : channels) {
      List<Message> messages = messageRepository.findByChannelId(channel.getId());
      Instant latestMessageTime = messages.stream()
          .max(Comparator.comparing(Message::getCreatedAt))
          .map(Message::getCreatedAt)
          .orElse(channel.getCreatedAt());
      if (channel.getType() == Type.PUBLIC) {
        channelDetailResponses.add(
            ChannelDetailResponse.of(channel, latestMessageTime, null));
      } else {
        List<UUID> participantIds = readStatusRepository.findByChannelId(channel.getId()).stream()
            .map(ReadStatus::getUserId)
            .toList();
        channelDetailResponses.add(
            ChannelDetailResponse.of(channel, latestMessageTime, participantIds));
      }
    }

    return channelDetailResponses;
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
    channelRepository.updateChannel(channel);
    return channel;
  }

  @Override
  public void deleteChannel(UUID channelId) {
    List<Message> messages = messageRepository.findByChannelId(channelId);

    for (Message message : messages) {
      message.getAttachmentIds().stream()
          .map(binaryContentRepository::findById)
          .filter(Optional::isPresent)
          .map(Optional::get)
          .forEach(attachment -> {
            fileManager.deleteFile(Path.of(attachment.getPath()));
            binaryContentRepository.delete(attachment.getId());
          });
    }

    messageRepository.deleteByChannelId(channelId);
    readStatusRepository.deleteByChannelId(channelId);
    channelRepository.deleteChannel(channelId);
  }
}