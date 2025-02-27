package com.sprint.mission.discodeit.docs;

import com.sprint.mission.discodeit.dto.request.ReadStatusRequest;
import com.sprint.mission.discodeit.dto.response.ReadStatusResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Read_status API")
public interface ReadStatusControllerDocs {

  @Operation(summary = "read_status 생성", description = "채널 id와 유저 id를 받아서 read_status 생성")
  @PostMapping
  ResponseEntity<Void> create(
      @RequestBody ReadStatusRequest readStatusRequest
  );

  @Operation(summary = "read_status 조회", description = "id에 해당하는 read_status를 조회")
  @GetMapping("/{id}")
  ResponseEntity<ReadStatusResponse> find(
      @Parameter(description = "read_status id") @PathVariable UUID id
  );

  @Operation(summary = "read_status 수정", description = "id에 해당하는 read_status를 수정")
  @PutMapping("/{id}")
  ResponseEntity<Void> update(
      @Parameter(description = "read_status id") @PathVariable UUID id
  );
}
