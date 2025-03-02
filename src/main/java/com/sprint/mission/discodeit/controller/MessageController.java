package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.docs.MessageControllerDocs;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.response.MessageDetailResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.basic.BinaryContentService;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/messages")
public class MessageController implements MessageControllerDocs {

  private final MessageService messageService;
  private final BinaryContentService binaryContentService;

  @PostMapping
  @Override
  public ResponseEntity<MessageDetailResponse> createMessage(
      @RequestPart MessageCreateRequest messageCreateRequest,
      @RequestPart(required = false) List<MultipartFile> attachments
  ) {
    List<BinaryContent> binaryContents = binaryContentService.create(attachments);
    Message message = messageService.createMessage(messageCreateRequest, binaryContents);
    return ResponseEntity.created(URI.create("messages/" + message.getId()))
        .body(MessageDetailResponse.from(message));
  }

  @GetMapping("/{id}")
  @Override
  public ResponseEntity<MessageDetailResponse> getMessage(
      @PathVariable UUID id
  ) {
    Message message = messageService.readMessage(id);
    return ResponseEntity.ok(MessageDetailResponse.from(message));
  }

  @GetMapping
  @Override
  public ResponseEntity<List<MessageDetailResponse>> getMessages(
      @RequestParam UUID channelId
  ) {
    return ResponseEntity.ok(messageService.readAllByChannelId(channelId));
  }

  @PatchMapping("/{id}")
  @Override
  public ResponseEntity<MessageDetailResponse> updateMessage(
      @PathVariable UUID id,
      @RequestBody MessageUpdateRequest messageUpdateRequest
  ) {
    Message message = messageService.updateMessage(id, messageUpdateRequest.newContent());
    return ResponseEntity.ok(MessageDetailResponse.from(message));
  }

  @DeleteMapping("/{id}")
  @Override
  public ResponseEntity<Void> deleteMessage(
      @PathVariable UUID id
  ) {
    messageService.deleteMessage(id);
    return ResponseEntity.noContent().build();
  }
}
