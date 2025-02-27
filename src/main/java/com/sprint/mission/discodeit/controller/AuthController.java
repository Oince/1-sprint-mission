package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.docs.AuthControllerDocs;
import com.sprint.mission.discodeit.dto.request.UserLoginRequest;
import com.sprint.mission.discodeit.dto.response.UserDetailResponse;
import com.sprint.mission.discodeit.service.basic.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController implements AuthControllerDocs {

  private final AuthService authService;

  @Operation(summary = "유저 로그인", description = "이름과 패스워드로 로그인.")
  @PostMapping("/login")
  @Override
  public ResponseEntity<UserDetailResponse> login(@RequestBody UserLoginRequest userLoginRequest,
      HttpSession session) {
    UserDetailResponse login = authService.login(userLoginRequest);
    return ResponseEntity.ok(login);
  }
}
