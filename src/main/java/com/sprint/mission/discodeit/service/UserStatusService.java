package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.response.UserStatusResponse;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.user.userstatus.UserStatusNotFoundException;
import com.sprint.mission.discodeit.mapper.UserStatusMapper;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserStatusService {

  private final UserStatusRepository userStatusRepository;
  private final UserStatusMapper userStatusMapper;

  @Transactional
  public UserStatusResponse update(UUID userId, Instant lastActiveAt) {
    UserStatus userStatus = userStatusRepository.findByUserId(userId)
        .orElseThrow(() -> new UserStatusNotFoundException(Map.of("userId", userId)));
    userStatus.updateLastActiveAt(lastActiveAt);
    userStatusRepository.save(userStatus);
    return userStatusMapper.toDto(userStatus);
  }

  public UserStatusResponse findById(UUID id) {
    UserStatus userStatus = userStatusRepository.findById(id)
        .orElseThrow(() -> new UserStatusNotFoundException(Map.of("id", id)));
    return userStatusMapper.toDto(userStatus);
  }

  public List<UserStatusResponse> findAll() {
    return userStatusRepository.findAllWithUser().stream()
        .map(userStatusMapper::toDto)
        .toList();
  }

  @Transactional
  public void delete(UUID id) {
    userStatusRepository.deleteById(id);
  }
}
