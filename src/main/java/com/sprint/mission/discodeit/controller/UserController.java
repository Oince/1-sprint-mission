package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.docs.UserControllerDocs;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.response.UserResponse;
import com.sprint.mission.discodeit.dto.response.UserStatusResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController implements UserControllerDocs {

  private final UserService userService;
  private final UserStatusService userStatusService;
  private final BinaryContentService binaryContentService;

  @PostMapping
  @Override
  public ResponseEntity<UserResponse> createUser(
      @RequestPart UserCreateRequest userCreateRequest,
      @RequestPart(required = false) MultipartFile profile
  ) {
    UserResponse userResponse = userService.createUser(userCreateRequest, profile);
    return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
  }

  @GetMapping
  @Override
  public ResponseEntity<List<UserResponse>> getUsers() {
    return ResponseEntity.ok(userService.readAll());
  }

  @PatchMapping("/{id}")
  @Override
  public ResponseEntity<UserResponse> updateUser(
      @PathVariable UUID id,
      @RequestPart UserUpdateRequest userUpdateRequest,
      @RequestPart(required = false) MultipartFile profile
  ) {
    BinaryContent content = null;
    if (profile != null) {
      content = binaryContentService.create(profile);
    }
    UserResponse userResponse = userService.updateUser(id, userUpdateRequest, content);
    return ResponseEntity.ok().body(userResponse);
  }

  @PatchMapping("/{id}/userStatus")
  @Override
  public ResponseEntity<UserStatusResponse> updateUserOnline(
      @PathVariable UUID id,
      @RequestBody UserStatusUpdateRequest userStatusUpdateRequest
  ) {
    UserStatusResponse userStatusResponse = userStatusService
        .update(id, userStatusUpdateRequest.newLastActiveAt());
    return ResponseEntity.ok().body(userStatusResponse);
  }

  @DeleteMapping("/{id}")
  @Override
  public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
    userService.deleteUser(id);
    return ResponseEntity.noContent().build();
  }
}
