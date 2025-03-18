package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.response.MessageResponse;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.NotFoundException;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MessageService {

  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;
  private final MessageRepository messageRepository;
  private final ReadStatusRepository readStatusRepository;
  private final BinaryContentStorage binaryContentStorage;
  private final MessageMapper messageMapper;
  private final PageResponseMapper pageResponseMapper;

  @Transactional
  public MessageResponse createMessage(MessageCreateRequest messageCreateRequest,
      List<BinaryContent> attachments) {
    UUID authorId = messageCreateRequest.authorId();
    UUID channelId = messageCreateRequest.channelId();

    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> new NotFoundException("등록되지 않은 channel. id=" + channelId));
    User user = userRepository.findById(authorId)
        .orElseThrow(() -> new NotFoundException("등록되지 않은 user. id=" + authorId));

    if (channel.getType() == Channel.Type.PRIVATE) {
      if (!readStatusRepository.existsByUser_IdAndChannel_Id(authorId, channelId)) {
        throw new NotFoundException("채널에 등록되지 않은 user. id=" + authorId);
      }
    }

    Message message = Message.create(user, messageCreateRequest.content(), channel, attachments);
    messageRepository.save(message);
    return messageMapper.toDto(message);
  }

  public MessageResponse readMessage(UUID messageId) {
    Message message = messageRepository.findById(messageId)
        .orElseThrow(() -> new NotFoundException("등록되지 않은 message. id=" + messageId));
    return messageMapper.toDto(message);
  }

  public PageResponse<MessageResponse> readAllByChannelId(
      UUID channelId, Instant cursor, Pageable pageable
  ) {
    PageRequest pageRequest = PageRequest.of(0, pageable.getPageSize(), pageable.getSort());

    Slice<MessageResponse> slice;
    if (cursor == null) {
      slice = messageRepository.findPageByChannel_Id(channelId, pageRequest)
          .map(messageMapper::toDto);
    } else {
      slice = messageRepository
          .findPageByChannel_IdWithCursor(channelId, cursor, pageRequest)
          .map(messageMapper::toDto);
    }

    List<MessageResponse> content = slice.getContent();
    Instant nextCursor;
    if (content.isEmpty()) {
      nextCursor = Instant.now();
    } else {
      nextCursor = content.get(content.size() - 1).createdAt();
    }
    return pageResponseMapper.fromSlice(slice, nextCursor);
  }

  @Transactional
  public MessageResponse updateMessage(UUID messageId, String content) {
    Message message = messageRepository.findById(messageId)
        .orElseThrow(() -> new NotFoundException("등록되지 않은 message. id=" + messageId));
    message.updateContent(content);
    messageRepository.save(message);
    return messageMapper.toDto(message);
  }

  @Transactional
  public void deleteMessage(UUID messageId) {
    messageRepository.findByIdWithAttachments(messageId)
        .ifPresent(message -> {
          message.getAttachments()
              .forEach(attachment -> binaryContentStorage.delete(attachment.getId()));
          messageRepository.delete(message);
        });
  }
}