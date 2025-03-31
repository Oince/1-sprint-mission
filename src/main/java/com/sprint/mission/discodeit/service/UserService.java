package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.response.UserResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.DuplicateException;
import com.sprint.mission.discodeit.exception.FileIOException;
import com.sprint.mission.discodeit.exception.NotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

  private final UserRepository userRepository;
  private final UserStatusRepository userStatusRepository;
  private final BinaryContentRepository binaryContentRepository;
  private final BinaryContentStorage binaryContentStorage;
  private final UserMapper userMapper;

  @Transactional
  public UserResponse createUser(UserCreateRequest userCreateRequest, MultipartFile profile) {
    duplicationCheck(userCreateRequest.username(), userCreateRequest.email());

    User newUser = User.create(userCreateRequest.username(), userCreateRequest.email(),
        userCreateRequest.password());
    BinaryContent content = null;
    if (profile != null && !profile.isEmpty()) {
      long size = profile.getSize();
      String fileName = profile.getOriginalFilename();
      String contentType = fileName.substring(fileName.lastIndexOf('.'));

      content = binaryContentRepository
          .save(BinaryContent.create(size, fileName, contentType));
      try {
        binaryContentStorage.put(content.getId(), profile.getBytes());
      } catch (IOException e) {
        throw new FileIOException("파일 생성 실패");
      }
    }
    newUser.updateProfile(content);
    userRepository.save(newUser);
    userStatusRepository.save(UserStatus.create(newUser));

    return userMapper.toDto(newUser);
  }

  public UserResponse readUser(UUID userId) {
    User user = userRepository.findByIdWithProfileAndStatus(userId)
        .orElseThrow(() -> new NotFoundException("등록되지 않은 user. id=" + userId));
    return userMapper.toDto(user);
  }

  public List<UserResponse> readAll() {
    return userRepository.findAllWithProfileAndStatus().stream()
        .map(userMapper::toDto)
        .toList();
  }

  @Transactional
  public UserResponse updateUser(UUID userId, UserUpdateRequest userUpdateRequest,
      BinaryContent binaryContent) {
    String newEmail = userUpdateRequest.newUsername();
    String newUsername = userUpdateRequest.newUsername();
    String newPassword = userUpdateRequest.newPassword();
    User user = userRepository.findByIdWithProfile(userId)
        .orElseThrow(() -> new NotFoundException("등록되지 않은 user. id=" + userId));

    if (newEmail != null) {
      if (!user.getEmail().equals(newEmail) && userRepository.existsByEmail(newEmail)) {
        throw new DuplicateException("중복된 이메일");
      }
      user.updateEmail(newEmail);
    }
    if (newUsername != null) {
      if (!user.getUsername().equals(newUsername) && userRepository.existsByUsername(newUsername)) {
        throw new DuplicateException("중복된 이름");
      }
      user.updateName(newUsername);
    }
    if (newPassword != null) {
      user.updatePassword(newPassword);
    }
    if (binaryContent != null) {
      user.getProfile().ifPresent(content -> binaryContentStorage.delete(content.getId()));
      user.updateProfile(binaryContent);
    }

    userRepository.save(user);
    return userMapper.toDto(user);
  }

  @Transactional
  public void deleteUser(UUID userId) {
    userRepository.findByIdWithProfile(userId)
        .ifPresent(user -> {
          user.getProfile().ifPresent(content -> binaryContentStorage.delete(content.getId()));
          userRepository.delete(user);
        });
  }

  private void duplicationCheck(String username, String email) {
    if (userRepository.existsByEmail(email) || userRepository.existsByUsername(username)) {
      throw new DuplicateException("중복된 이름 혹은 이메일 입니다.");
    }
  }
}