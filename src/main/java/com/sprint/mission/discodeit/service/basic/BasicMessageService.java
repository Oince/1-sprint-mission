package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.response.MessageDetailResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.NotFoundException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.file.FileManager;
import com.sprint.mission.discodeit.service.MessageService;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

  private final ChannelRepository channelRepository;
  private final MessageRepository messageRepository;
  private final BinaryContentRepository binaryContentRepository;
  private final FileManager fileManager;

  @Override
  public Message createMessage(MessageCreateRequest messageCreateRequest,
      List<BinaryContent> attachments) {
    Channel channel = channelRepository.findById(messageCreateRequest.channelId())
        .orElseThrow(
            () -> new NotFoundException("등록되지 않은 channel. id=" + messageCreateRequest.channelId()));
    User user = channel.getUser(messageCreateRequest.writer());
    List<UUID> attachmentIds = attachments.stream()
        .map(BinaryContent::getId)
        .toList();
    Message message = Message.of(user, messageCreateRequest.content(), channel, attachmentIds);
    return messageRepository.save(message);
  }

  @Override
  public Message readMessage(UUID messageId) {
    return messageRepository.findById(messageId)
        .orElseThrow(() -> new NotFoundException("등록되지 않은 message. id=" + messageId));
  }

  @Override
  public List<MessageDetailResponse> readAllByChannelId(UUID channelId) {
    return messageRepository.findByChannelId(channelId).stream()
        .map(MessageDetailResponse::from)
        .toList();
  }

  @Override
  public void updateMessage(UUID messageId, String content) {
    Message message = messageRepository.findById(messageId)
        .orElseThrow(() -> new NotFoundException("등록되지 않은 message. id=" + messageId));
    message.updateContent(content);
    messageRepository.updateMessage(message);
  }

  @Override
  public void deleteMessage(UUID messageId) {
    Message message = messageRepository.findById(messageId)
        .orElseThrow(() -> new NotFoundException("등록되지 않은 message. id=" + messageId));

    message.getAttachmentIds().stream()
        .map(attachmentId -> binaryContentRepository.findById(attachmentId)
            .orElseThrow(() -> new NotFoundException("등록되지 않은 binary content. id=" + attachmentId)))
        .forEach(content -> {
          binaryContentRepository.delete(content.getId());
          fileManager.deleteFile(Path.of(content.getPath()));
        });

    messageRepository.deleteMessage(messageId);
  }
}