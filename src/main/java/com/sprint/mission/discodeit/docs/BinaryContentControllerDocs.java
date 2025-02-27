package com.sprint.mission.discodeit.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.MalformedURLException;
import java.util.UUID;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "Binary_content API", description = "파일 관리 API")
public interface BinaryContentControllerDocs {

  @Operation(summary = "파일 조회", description = "파일 하나를 조회")
  @GetMapping("/{id}")
  ResponseEntity<Resource> getFileById(@PathVariable UUID id) throws MalformedURLException;
}
