package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.docs.UserControllerDocs;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.response.UserDetailResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.basic.BinaryContentService;
import com.sprint.mission.discodeit.service.basic.UserStatusService;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
  public ResponseEntity<Void> createUser(
      @RequestPart UserCreateRequest userCreateRequest,
      @RequestPart(required = false) MultipartFile profile
  ) {
    User user = userService.createUser(userCreateRequest);
    if (profile != null) {
      BinaryContent content = binaryContentService.create(profile);
      user.updateProfile(content.getId());
    }
    return ResponseEntity.created(URI.create("/users/" + user.getId())).build();
  }

  @GetMapping
  @Override
  public ResponseEntity<List<UserDetailResponse>> getUsers() {
    return ResponseEntity.ok(userService.readAll());
  }

  @GetMapping("/{id}")
  @Override
  public ResponseEntity<UserDetailResponse> getUser(@PathVariable UUID id) {
    return ResponseEntity.ok(userService.readUser(id));
  }

  @PutMapping("/{id}")
  @Override
  public ResponseEntity<Void> updateUser(@PathVariable UUID id,
      @RequestBody UserCreateRequest userCreateRequest) {
    userService.updateUser(id, userCreateRequest);
    return ResponseEntity.ok().build();
  }

  @PatchMapping("/{id}")
  @Override
  public ResponseEntity<Void> updateUserOnline(@PathVariable UUID id) {
    userStatusService.update(id);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/{id}")
  @Override
  public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
    userService.deleteUser(id);
    return ResponseEntity.noContent().build();
  }
}
