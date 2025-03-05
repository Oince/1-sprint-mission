package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.docs.BinaryContentControllerDocs;
import com.sprint.mission.discodeit.dto.response.BinaryContentResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.file.FileManager;
import com.sprint.mission.discodeit.service.basic.BinaryContentService;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
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
  private final FileManager fileManager;

  @GetMapping("/{id}")
  @Override
  public ResponseEntity<BinaryContentResponse> getFileById(@PathVariable UUID id) {

    BinaryContent content = binaryContentService.find(id);
    Path path = Path.of(content.getPath());
    byte[] bytes = fileManager.readFile(path);

    return ResponseEntity.ok(BinaryContentResponse.of(content, bytes));
  }

  @GetMapping
  @Override
  public ResponseEntity<List<BinaryContentResponse>> getFiles(
      @RequestParam List<UUID> binaryContentIds) {
    ArrayList<BinaryContentResponse> responses = new ArrayList<>(100);

    binaryContentIds.stream()
        .map(binaryContentService::find)
        .forEach(content -> responses.add(
            BinaryContentResponse.of(content, fileManager.readFile(Path.of(content.getPath())))));

    return ResponseEntity.ok(responses);
  }
}