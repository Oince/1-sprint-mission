package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.response.UserDetailResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.DuplicateException;
import com.sprint.mission.discodeit.exception.NotFoundException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.repository.file.FileManager;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

  private final UserRepository userRepository;
  private final UserStatusRepository userStatusRepository;
  private final BinaryContentRepository binaryContentRepository;
  private final FileManager fileManager;

  @Override
  public User createUser(UserCreateRequest userCreateRequest, BinaryContent binaryContent) {
    duplicationCheck(userCreateRequest.username(), userCreateRequest.email());

    String password = generatePassword(userCreateRequest.password());
    User newUser = User.of(userCreateRequest.username(), userCreateRequest.email(), password);
    if (binaryContent != null) {
      newUser.updateProfile(binaryContent.getId());
    }

    userStatusRepository.save(UserStatus.from(newUser.getId()));
    return userRepository.save(newUser);
  }

  @Override
  public UserDetailResponse readUser(UUID userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new NotFoundException("등록되지 않은 user. id=" + userId));
    UserStatus userStatus = userStatusRepository.findById(user.getId())
        .orElseThrow(() -> new NotFoundException("등록되지 않은 userStatus. id=" + userId));
    return UserDetailResponse.of(user, userStatus.isOnline());
  }

  @Override
  public List<UserDetailResponse> readAll() {
    List<User> users = userRepository.findAll();
    List<UserDetailResponse> userDetailResponses = new ArrayList<>(100);
    for (User user : users) {
      UserStatus userStatus = userStatusRepository.findById(user.getId())
          .orElseThrow(() -> new NotFoundException("등록되지 않은 userStatus. id=" + user.getId()));
      UserDetailResponse userDetailResponse = UserDetailResponse.of(user, userStatus.isOnline());
      userDetailResponses.add(userDetailResponse);
    }
    return userDetailResponses;
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
      user.updatePassword(generatePassword(userUpdateRequest.newPassword()));
    }
    if (binaryContent != null) {
      user.updateProfile(binaryContent.getId());
    }

    userRepository.updateUser(user);
    return user;
  }

  @Override
  public void deleteUser(UUID userId) {
    UUID profileId = userRepository.findById(userId)
        .orElseThrow(() -> new NotFoundException("등록되지 않은 user. id=" + userId))
        .getProfileId();

    Optional<BinaryContent> binaryContent = binaryContentRepository.findById(profileId);
    if (binaryContent.isPresent()) {
      BinaryContent content = binaryContent.get();
      fileManager.deleteFile(Path.of(content.getPath()));
    }
    userStatusRepository.delete(userId);
    userRepository.deleteUser(userId);
  }

  private void duplicationCheck(String username, String email) {
    List<User> users = userRepository.findAll();
    for (User user : users) {
      if (user.getUsername().equals(username) || user.getEmail().equals(email)) {
        throw new DuplicateException("중복된 이름 혹은 이메일 입니다.");
      }
    }

  }

  private String generatePassword(String password) {
    StringBuilder builder = new StringBuilder();
    try {
      MessageDigest md = MessageDigest.getInstance("SHA-256");
      byte[] digest = md.digest(password.getBytes());

      for (byte b : digest) {
        builder.append(String.format("%02x", b));
      }
    } catch (NoSuchAlgorithmException e) {
      throw new IllegalArgumentException("비밀번호 암호화 오류");
    }
    return builder.toString();
  }
}