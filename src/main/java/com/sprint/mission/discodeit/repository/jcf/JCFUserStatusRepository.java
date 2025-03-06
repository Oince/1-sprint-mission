package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf")
public class JCFUserStatusRepository implements UserStatusRepository {

  private final Map<UUID, UserStatus> data;

  public JCFUserStatusRepository() {
    data = new HashMap<>(100);
  }

  @Override
  public UserStatus save(UserStatus userStatus) {
    data.put(userStatus.getId(), userStatus);
    return userStatus;
  }

  @Override
  public Optional<UserStatus> findById(UUID id) {
    return Optional.ofNullable(data.get(id));
  }

  @Override
  public Optional<UserStatus> findByUserId(UUID userId) {
    return data.values().stream()
        .filter(userStatus -> userStatus.getUserId().equals(userId))
        .findFirst();
  }

  @Override
  public List<UserStatus> findAll() {
    return new ArrayList<>(data.values());
  }

  @Override
  public void delete(UUID id) {
    data.remove(id);
  }

  @Override
  public void deleteByUserId(UUID userId) {
    data.values().removeIf(userStatus -> userStatus.getUserId().equals(userId));
  }
}
