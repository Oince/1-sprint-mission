package com.sprint.mission.discodeit.docs;

import com.sprint.mission.discodeit.dto.request.UserLoginRequest;
import com.sprint.mission.discodeit.dto.response.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Auth API", description = "인증 관리 API")
public interface AuthControllerDocs {

  @Operation(summary = "유저 로그인")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "로그인 성공"),
      @ApiResponse(responseCode = "400", description = "비밀번호가 일치하지 않음"),
      @ApiResponse(responseCode = "404", description = "유저를 찾을 수 없음")
  })
  @PostMapping("/login")
  ResponseEntity<UserResponse> login(@RequestBody UserLoginRequest userLoginRequest);
}
