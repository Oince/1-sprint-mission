package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.DuplicateException;
import com.sprint.mission.discodeit.exception.NotFoundException;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

  private final UserRepository userRepository;
  private final BinaryContentStorage binaryContentStorage;

  @Transactional
  @Override
  public User createUser(UserCreateRequest userCreateRequest, BinaryContent binaryContent) {
    duplicationCheck(userCreateRequest.username(), userCreateRequest.email());

    User newUser = User.create(userCreateRequest.username(), userCreateRequest.email(),
        userCreateRequest.password());
    if (binaryContent != null) {
      newUser.updateProfile(binaryContent);
    }

    return userRepository.save(newUser);
  }

  @Override
  public User readUser(UUID userId) {
    return userRepository.findById(userId)
        .orElseThrow(() -> new NotFoundException("등록되지 않은 user. id=" + userId));
  }

  @Override
  public List<User> readAll() {
    return userRepository.findAll();
  }

  @Transactional
  @Override
  public User updateUser(UUID userId, UserUpdateRequest userUpdateRequest,
      BinaryContent binaryContent) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new NotFoundException("등록되지 않은 user. id=" + userId));

    List<User> users = userRepository.findAll();
    if (userUpdateRequest.newEmail() != null) {
      users.stream()
          .filter(u -> u.getEmail().equals(userUpdateRequest.newEmail()))
          .findAny()
          .ifPresent(u -> {
            throw new DuplicateException("중복된 이메일");
          });
      user.updateEmail(userUpdateRequest.newEmail());
    }
    if (userUpdateRequest.newUsername() != null) {
      users.stream()
          .filter(u -> u.getUsername().equals(userUpdateRequest.newUsername()))
          .findAny()
          .ifPresent(u -> {
            throw new DuplicateException("중복된 이름");
          });
      user.updateName(userUpdateRequest.newUsername());
    }
    if (userUpdateRequest.newPassword() != null) {
      user.updatePassword(userUpdateRequest.newPassword());
    }
    if (binaryContent != null) {
      user.getProfile().ifPresent(content -> binaryContentStorage.delete(content.getId()));
      user.updateProfile(binaryContent);
    }

    userRepository.save(user);
    return user;
  }

  @Transactional
  @Override
  public void deleteUser(UUID userId) {
    userRepository.findById(userId)
        .ifPresent(user -> {
          user.getProfile().ifPresent(content -> binaryContentStorage.delete(content.getId()));
          userRepository.delete(user);
        });
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