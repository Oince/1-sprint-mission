package com.sprint.mission.discodeit.docs;

import com.sprint.mission.discodeit.dto.request.PrivateChannelRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelRequest;
import com.sprint.mission.discodeit.dto.response.ChannelDetailResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Channel API", description = "채널 관리 API")
public interface ChannelControllerDocs {

  @Operation(summary = "퍼블릭 채널 생성", description = "모든 유저가 접근할 수 있는 퍼블릭 채널 생성")
  @PostMapping("/public")
  ResponseEntity<Void> createChannel(
      @RequestBody PublicChannelRequest publicChannelRequest
  );

  @Operation(summary = "프라이빗 채널 생성", description = "지정한 유저만 접근할 수 있는 프라이빗 채널 생성")
  @PostMapping("/private")
  ResponseEntity<Void> createPrivateChannel(
      @RequestBody PrivateChannelRequest privateChannelRequest
  );

  @Operation(summary = "채널 조회", description = "유저가 접근할 수 있는 채널 조회")
  @GetMapping
  ResponseEntity<List<ChannelDetailResponse>> getChannels(
      @Parameter(description = "유저 id") @RequestParam UUID userId
  );

  @Operation(summary = "채널 수정", description = "id에 해당하는 채널에 대한 정보를 수정함, 프라이빗 채널은 수정 불가능")
  @PutMapping("/public/{id}")
  ResponseEntity<Void> updatePublicChannel(
      @Parameter(description = "채널 id") @PathVariable UUID id,
      @RequestBody PublicChannelRequest publicChannelRequest
  );

  @Operation(summary = "채널에 유저 추가", description = "id에 해당하는 채널에 유저 추가")
  @PatchMapping("/{id}/users/{userId}")
  ResponseEntity<Void> addUser(
      @Parameter(description = "채널 id") @PathVariable UUID id,
      @Parameter(description = "유저 id") @PathVariable UUID userId
  );

  @Operation(summary = "채널의 유저 삭제", description = "id에 해당하는 채널에 유저 삭제")
  @DeleteMapping("/{id}/users/{userId}")
  ResponseEntity<Void> deleteUser(
      @Parameter(description = "채널 id") @PathVariable UUID id,
      @Parameter(description = "유저 id") @PathVariable UUID userId
  );

  @Operation(summary = "채널 삭제", description = "id에 해당하는 채널 삭제")
  @DeleteMapping("/{id}")
  ResponseEntity<Void> deleteChannel(
      @Parameter(description = "채널 id") @PathVariable UUID id
  );
}
