package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.response.UserStatusResponse;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.DuplicateException;
import com.sprint.mission.discodeit.exception.NotFoundException;
import com.sprint.mission.discodeit.mapper.UserStatusMapper;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import java.time.Instant;
import java.util.List;
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
  public UserStatusResponse create(User user) {
    if (userStatusRepository.existsByUser(user)) {
      throw new DuplicateException("이미 존재하는 user에 대한 userStatus 생성");
    }

    UserStatus userStatus = userStatusRepository.save(UserStatus.create(user));
    return userStatusMapper.toDto(userStatus);
  }

  @Transactional
  public UserStatusResponse update(User user, Instant lastActiveAt) {
    UserStatus userStatus = userStatusRepository.findByUser(user)
        .orElseThrow(() -> new NotFoundException("등록되지 않은 user에 대한 userStatus 접근"));
    userStatus.updateLastActiveAt(lastActiveAt);
    userStatus = userStatusRepository.save(userStatus);
    return userStatusMapper.toDto(userStatus);
  }

  public UserStatus findById(UUID id) {
    return userStatusRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("등록되지 않은 userStatus. id=" + id));
  }

  public List<UserStatus> findAll() {
    return userStatusRepository.findAllWithUser();
  }

  @Transactional
  public void delete(UUID id) {
    userStatusRepository.deleteById(id);
  }
}
