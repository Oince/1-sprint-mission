package com.sprint.mission.discodeit.docs;

import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.response.MessageDetailResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Message API", description = "메세지 관리 API")
public interface MessageControllerDocs {

  @Operation(summary = "메세지 생성", description = "메세지 생성")
  ResponseEntity<Void> createMessage(
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
          content = @Content(
              schema = @Schema(implementation = MessageCreateRequest.class)
          )
      )
      @RequestBody MessageCreateRequest messageCreateRequest,
      @RequestPart(required = false) List<MultipartFile> attachments
  );

  @Operation(summary = "메세지 조회", description = "id에 해당하는 메세지 조회")
  ResponseEntity<MessageDetailResponse> getMessage(
      @Parameter(description = "메세지 id") @PathVariable UUID id
  );

  @Operation(summary = "채널의 모든 메세지 조회", description = "채널의 모든 메세지 조회")
  ResponseEntity<List<MessageDetailResponse>> getMessages(
      @Parameter(description = "채널 id") @PathVariable UUID channelId
  );

  @Operation(summary = "메세지 수정", description = "id에 해당하는 메세지 내용 수정")
  ResponseEntity<Void> updateMessage(
      @Parameter(description = "메세지 id") UUID id,
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
          content = @Content(
              schema = @Schema(implementation = MessageUpdateRequest.class)
          )
      )
      @RequestBody MessageUpdateRequest messageUpdateRequest
  );

  @Operation(summary = "메세지 삭제", description = "id에 해당하는 메세지 삭제")
  ResponseEntity<Void> deleteMessage(
      @Parameter(description = "메세지 id") @PathVariable UUID id
  );
}
