package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.NotFoundException;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;
  private final MessageRepository messageRepository;
  private final ReadStatusRepository readStatusRepository;
  private final BinaryContentStorage binaryContentStorage;

  @Transactional
  @Override
  public Message createMessage(MessageCreateRequest messageCreateRequest,
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
    return messageRepository.save(message);
  }

  @Override
  public Message readMessage(UUID messageId) {
    return messageRepository.findById(messageId)
        .orElseThrow(() -> new NotFoundException("등록되지 않은 message. id=" + messageId));
  }

  @Override
  public Page<Message> readAllByChannelId(UUID channelId, Pageable pageable) {
    return messageRepository.findPageByChannel_Id(channelId, pageable);
  }

  @Transactional
  @Override
  public Message updateMessage(UUID messageId, String content) {
    Message message = messageRepository.findById(messageId)
        .orElseThrow(() -> new NotFoundException("등록되지 않은 message. id=" + messageId));
    message.updateContent(content);
    messageRepository.save(message);
    return message;
  }

  @Transactional
  @Override
  public void deleteMessage(UUID messageId) {
    messageRepository.findById(messageId)
        .ifPresent(message -> {
          message.getAttachments()
              .forEach(attachment -> binaryContentStorage.delete(attachment.getId()));
          messageRepository.delete(message);
        });
  }
}