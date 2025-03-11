package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.response.UserResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import java.util.List;
import java.util.UUID;

public interface UserService {

  User createUser(UserCreateRequest userCreateRequest, BinaryContent binaryContent);

  User findById(UUID id);

  UserResponse readUser(UUID userId);

  List<UserResponse> readAll();

  User updateUser(UUID userId, UserUpdateRequest userUpdateRequest, BinaryContent binaryContent);

  void deleteUser(UUID userId);
}