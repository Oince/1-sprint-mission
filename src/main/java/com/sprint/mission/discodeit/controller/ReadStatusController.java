package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.docs.ReadStatusControllerDocs;
import com.sprint.mission.discodeit.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.response.ReadStatusResponse;
import com.sprint.mission.discodeit.service.ReadStatusService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
      @RequestBody @Valid ReadStatusCreateRequest readStatusCreateRequest
  ) {
    ReadStatusResponse readStatus = readStatusService.create(readStatusCreateRequest);
    return ResponseEntity.status(HttpStatus.CREATED).body(readStatus);
  }

  @GetMapping
  @Override
  public ResponseEntity<List<ReadStatusResponse>> find(@RequestParam UUID userId) {
    return ResponseEntity.ok(readStatusService.findAllByUserId(userId));
  }

  @PatchMapping("/{id}")
  @Override
  public ResponseEntity<ReadStatusResponse> update(@PathVariable UUID id,
      @RequestBody @Valid ReadStatusUpdateRequest readStatusUpdateRequest) {
    ReadStatusResponse readStatusResponse = readStatusService.update(id,
        readStatusUpdateRequest.newLastReadAt());
    return ResponseEntity.ok(readStatusResponse);
  }
}
