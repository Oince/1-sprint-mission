package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.docs.BinaryContentControllerDocs;
import com.sprint.mission.discodeit.dto.response.BinaryContentResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/binaryContents")
@RequiredArgsConstructor
public class BinaryContentController implements BinaryContentControllerDocs {

  private final BinaryContentService binaryContentService;
  private final BinaryContentStorage binaryContentStorage;

  @GetMapping("/{id}")
  @Override
  public ResponseEntity<BinaryContentResponse> getBinaryContentById(@PathVariable UUID id) {

    BinaryContent content = binaryContentService.find(id);

    return ResponseEntity.ok(BinaryContentResponse.from(content));
  }

  @GetMapping
  @Override
  public ResponseEntity<List<BinaryContentResponse>> getBinaryContents(
      @RequestParam List<UUID> binaryContentIds) {
    ArrayList<BinaryContentResponse> responses = new ArrayList<>(100);

    binaryContentIds.stream()
        .map(binaryContentService::find)
        .forEach(content ->
            responses.add(BinaryContentResponse.from(content)));

    return ResponseEntity.ok(responses);
  }

  @GetMapping("/{id}/download")
  public ResponseEntity<Resource> getFile(@PathVariable UUID id) {
    BinaryContent content = binaryContentService.find(id);
    BinaryContentResponse response = BinaryContentResponse.from(content);
    return binaryContentStorage.download(response);
  }
}