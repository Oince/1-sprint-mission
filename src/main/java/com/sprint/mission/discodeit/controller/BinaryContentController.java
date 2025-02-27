package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.docs.BinaryContentControllerDocs;
import com.sprint.mission.discodeit.service.basic.BinaryContentService;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.UriUtils;

@Controller
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class BinaryContentController implements BinaryContentControllerDocs {

  private final BinaryContentService binaryContentService;

  @GetMapping("/{id}")
  @Override
  public ResponseEntity<Resource> getFileById(@PathVariable UUID id) throws MalformedURLException {

    Path path = binaryContentService.find(id);
    UrlResource resource = new UrlResource("file:" + path);

    String encode = UriUtils.encode(path.getFileName().toString(), StandardCharsets.UTF_8);
    String contentDisposition = "attachment; filename=\"" + encode + "\"";

    return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
        .body(resource);
  }
}