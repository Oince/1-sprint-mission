package com.sprint.mission.discodeit.docs;

import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.response.UserDetailResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "User API", description = "유저 관리 API")
public interface UserControllerDocs {

  @Operation(summary = "유저 생성", description = "유저 생성")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "유저 생성 성공",
          headers = @Header(name = HttpHeaders.LOCATION, description = "생성된 유저의 uri", example = "/users/")),
      @ApiResponse(responseCode = "409", description = "중복된 이름 또는 이메일")
  })
  @PostMapping
  ResponseEntity<Void> createUser(
      @RequestPart("userCreateRequest") UserCreateRequest userCreateRequest,
      @RequestPart(required = false) MultipartFile profile
  );

  @Operation(summary = "모든 유저 조회", description = "등록된 모든 유저를 조회")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "모든 유저 조회 성공")
  })
  @GetMapping
  ResponseEntity<List<UserDetailResponse>> getUsers();

  @Operation(summary = "유저 조회", description = "id에 해당하는 유저 하나를 조회")
  @GetMapping("/{id}")
  ResponseEntity<UserDetailResponse> getUser(
      @Parameter(description = "유저의 id") @PathVariable UUID id
  );

  @Operation(summary = "유저 수정", description = "id에 해당하는 유저에 대한 정보를 수정")
  @PutMapping("/{id}")
  ResponseEntity<Void> updateUser(
      @Parameter(description = "유저의 id") @PathVariable UUID id,
      @RequestBody UserCreateRequest userCreateRequest
  );

  @Operation(summary = "온라인 상태 변경", description = "id에 해당하는 유저를 온라인 상태로 변경")
  @PatchMapping("/{id}")
  ResponseEntity<Void> updateUserOnline(
      @Parameter(description = "유저의 id") @PathVariable UUID id
  );

  @Operation(summary = "유저 삭제", description = "id에 해당하는 유저를 삭제")
  @DeleteMapping("/{id}")
  ResponseEntity<Void> deleteUser(
      @Parameter(description = "유저의 id") @PathVariable UUID id
  );
}
