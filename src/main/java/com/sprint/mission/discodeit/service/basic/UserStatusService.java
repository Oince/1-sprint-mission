package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.DuplicateException;
import com.sprint.mission.discodeit.exception.NotFoundException;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserStatusService {

  private final UserStatusRepository userStatusRepository;
  private final UserRepository userRepository;

  public UserStatus create(UUID userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new NotFoundException("등록되지 않은 user. id=" + userId));
    List<UserStatus> userStatuses = userStatusRepository.findAll();

    for (UserStatus userStatus : userStatuses) {
      if (userStatus.getUser().getId().equals(userId)) {
        throw new DuplicateException("이미 존재하는 user에 대한 userStatus 생성");
      }
    }

    UserStatus userStatus = UserStatus.from(user);
    return userStatusRepository.save(userStatus);
  }

  public UserStatus update(UUID id, Instant lastActiveAt) {
    UserStatus userStatus = findById(id);
    userStatus.updateLastActiveAt(lastActiveAt);
    return userStatusRepository.save(userStatus);
  }

  public UserStatus findById(UUID id) {
    return userStatusRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("등록되지 않은 userStatus. id=" + id));
  }

  public List<UserStatus> findAll() {
    return userStatusRepository.findAll();
  }

  public void delete(UUID id) {
    userStatusRepository.deleteById(id);
  }
}
