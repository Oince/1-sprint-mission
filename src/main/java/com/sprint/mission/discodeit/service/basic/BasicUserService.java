package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.response.UserResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.DuplicateException;
import com.sprint.mission.discodeit.exception.NotFoundException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

  private final UserRepository userRepository;
  private final UserStatusRepository userStatusRepository;
  private final BinaryContentRepository binaryContentRepository;
  private final BinaryContentStorage binaryContentStorage;

  @Override
  public User createUser(UserCreateRequest userCreateRequest, BinaryContent binaryContent) {
    duplicationCheck(userCreateRequest.username(), userCreateRequest.email());

    User newUser = User.of(userCreateRequest.username(), userCreateRequest.email(),
        userCreateRequest.password());
    if (binaryContent != null) {
      newUser.updateProfile(binaryContent);
    }

    return userRepository.save(newUser);
  }

  @Override
  public User findById(UUID id) {
    return userRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("등록되지 않은 user. id: " + id));
  }

  @Override
  public UserResponse readUser(UUID userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new NotFoundException("등록되지 않은 user. id=" + userId));
    return UserResponse.from(user);
  }

  @Override
  public List<UserResponse> readAll() {
    List<User> users = userRepository.findAll();
    List<UserResponse> userResponses = new ArrayList<>(100);
    for (User user : users) {
      UserResponse userResponse = UserResponse.from(user);
      userResponses.add(userResponse);
    }
    return userResponses;
  }

  @Override
  public User updateUser(UUID userId, UserUpdateRequest userUpdateRequest,
      BinaryContent binaryContent) {
    duplicationCheck(userUpdateRequest.newUsername(), userUpdateRequest.newEmail());

    User user = userRepository.findById(userId)
        .orElseThrow(() -> new NotFoundException("등록되지 않은 user. id=" + userId));
    user.updateName(userUpdateRequest.newUsername());
    user.updateEmail(userUpdateRequest.newEmail());
    if (userUpdateRequest.newPassword() != null) {
      user.updatePassword(userUpdateRequest.newPassword());
    }
    if (binaryContent != null) {
      user.updateProfile(binaryContent);
    }

    userRepository.save(user);
    return user;
  }

  @Override
  public void deleteUser(UUID userId) {
    Optional<User> user = userRepository.findById(userId);
    if (user.isEmpty()) {
      return;
    }

    BinaryContent profile = user.get().getProfile();
    Optional<BinaryContent> binaryContent = binaryContentRepository.findById(profile.getId());
    if (binaryContent.isPresent()) {
      BinaryContent content = binaryContent.get();
      binaryContentStorage.delete(content.getId());
      binaryContentRepository.deleteById(content.getId());
    }
    userStatusRepository.deleteById(userId);
    userRepository.deleteById(userId);
  }

  private void duplicationCheck(String username, String email) {
    List<User> users = userRepository.findAll();
    for (User user : users) {
      if (user.getUsername().equals(username) || user.getEmail().equals(email)) {
        throw new DuplicateException("중복된 이름 혹은 이메일 입니다.");
      }
    }
  }
}