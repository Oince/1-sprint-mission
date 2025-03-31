package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.docs.MessageControllerDocs;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.response.MessageResponse;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.MessageService;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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
  public ResponseEntity<MessageResponse> createMessage(
      @RequestPart MessageCreateRequest messageCreateRequest,
      @RequestPart(required = false) List<MultipartFile> attachments
  ) {
    MessageResponse messageResponse = messageService.createMessage(messageCreateRequest,
        attachments);
    return ResponseEntity.status(HttpStatus.CREATED).body(messageResponse);
  }

  @GetMapping
  @Override
  public ResponseEntity<PageResponse<MessageResponse>> getMessages(
      @RequestParam UUID channelId,
      @RequestParam(required = false) Instant cursor,
      Pageable pageable
  ) {
    return ResponseEntity
        .ok(messageService.readAllByChannelId(channelId, cursor, pageable));
  }

  @PatchMapping("/{id}")
  @Override
  public ResponseEntity<MessageResponse> updateMessage(
      @PathVariable UUID id,
      @RequestBody MessageUpdateRequest messageUpdateRequest
  ) {
    MessageResponse messageResponse = messageService.updateMessage(id,
        messageUpdateRequest.newContent());
    return ResponseEntity.ok(messageResponse);
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
