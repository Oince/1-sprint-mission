package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.response.MessageDetailResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import java.util.List;
import java.util.UUID;

public interface MessageService {

  Message createMessage(MessageCreateRequest messageCreateRequest, List<BinaryContent> attachments);

  Message readMessage(UUID messageId);

  List<MessageDetailResponse> readAllByChannelId(UUID channelId);

  void updateMessage(UUID messageId, String content);

  void deleteMessage(UUID messageId);
}
