package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.docs.ReadStatusControllerDocs;
import com.sprint.mission.discodeit.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.response.ReadStatusResponse;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.service.ReadStatusService;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/readStatuses")
public class ReadStatusController implements ReadStatusControllerDocs {

  private final ReadStatusService readStatusService;

  @PostMapping
  @Override
  public ResponseEntity<ReadStatusResponse> create(
      @RequestBody ReadStatusCreateRequest readStatusCreateRequest
  ) {
    ReadStatus readStatus = readStatusService.create(readStatusCreateRequest);
    return ResponseEntity.created(URI.create("/readStatuses/" + readStatus.getId()))
        .body(ReadStatusResponse.from(readStatus));
  }

  @GetMapping
  @Override
  public ResponseEntity<List<ReadStatusResponse>> find(@RequestParam UUID userId) {
    List<ReadStatusResponse> readStatusResponses = readStatusService.findAllByUserId(userId)
        .stream()
        .map(ReadStatusResponse::from)
        .toList();

    return ResponseEntity.ok(readStatusResponses);
  }

  @PatchMapping("/{id}")
  @Override
  public ResponseEntity<Void> update(@PathVariable UUID id,
      @RequestBody ReadStatusUpdateRequest readStatusUpdateRequest) {
    readStatusService.update(id, readStatusUpdateRequest.newLastReadAt());
    return ResponseEntity.ok().build();
  }
}
