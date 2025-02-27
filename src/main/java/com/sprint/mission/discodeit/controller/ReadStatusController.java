package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.docs.ReadStatusControllerDocs;
import com.sprint.mission.discodeit.dto.request.ReadStatusRequest;
import com.sprint.mission.discodeit.dto.response.ReadStatusResponse;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.service.basic.ReadStatusService;
import java.net.URI;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/read-statuses")
public class ReadStatusController implements ReadStatusControllerDocs {

  private final ReadStatusService readStatusService;

  @PostMapping
  @Override
  public ResponseEntity<Void> create(
      @RequestBody ReadStatusRequest readStatusRequest
  ) {
    ReadStatus readStatus = readStatusService.create(readStatusRequest);
    return ResponseEntity.created(URI.create("/read_statuses/" + readStatus.getId())).build();
  }

  @GetMapping("/{id}")
  @Override
  public ResponseEntity<ReadStatusResponse> find(@PathVariable UUID id) {
    ReadStatus readStatus = readStatusService.find(id);
    return ResponseEntity.ok(ReadStatusResponse.from(readStatus));
  }

  @PutMapping("/{id}")
  @Override
  public ResponseEntity<Void> update(@PathVariable UUID id) {
    readStatusService.update(id);
    return ResponseEntity.ok().build();
  }
}
