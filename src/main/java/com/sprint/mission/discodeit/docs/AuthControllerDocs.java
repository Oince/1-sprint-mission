package com.sprint.mission.discodeit.docs;

import com.sprint.mission.discodeit.dto.request.UserLoginRequest;
import com.sprint.mission.discodeit.dto.response.UserDetailResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Auth API", description = "인증 관리 API")
public interface AuthControllerDocs {

  @Operation(summary = "유저 로그인", description = "이름과 패스워드로 로그인.")
  @PostMapping("/login")
  ResponseEntity<UserDetailResponse> login(@RequestBody UserLoginRequest userLoginRequest,
      HttpSession session);
}
