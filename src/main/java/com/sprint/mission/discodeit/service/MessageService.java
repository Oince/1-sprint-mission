package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MessageService {

  Message createMessage(MessageCreateRequest messageCreateRequest, List<BinaryContent> attachments);

  Message readMessage(UUID messageId);

  Page<Message> readAllByChannelId(UUID channelId, Pageable pageable);

  Message updateMessage(UUID messageId, String content);

  void deleteMessage(UUID messageId);
}
