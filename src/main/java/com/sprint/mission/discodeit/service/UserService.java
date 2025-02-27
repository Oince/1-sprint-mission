package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.response.UserDetailResponse;
import com.sprint.mission.discodeit.entity.User;
import java.util.List;
import java.util.UUID;

public interface UserService {

  User createUser(UserCreateRequest userCreateRequest);

  UserDetailResponse readUser(UUID userId);

  List<UserDetailResponse> readAll();

  void updateUser(UUID userId, UserCreateRequest userCreateRequest);

  void deleteUser(UUID userId);
}