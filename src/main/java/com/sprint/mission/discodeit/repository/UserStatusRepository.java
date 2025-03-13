package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserStatusRepository extends JpaRepository<UserStatus, UUID> {

  @Query("SELECT status from UserStatus status "
      + "JOIN FETCH status.user "
      + "WHERE status.user = :user")
  Optional<UserStatus> findByUser(@Param("user") User user);

  @Query("SELECT status FROM UserStatus status "
      + "JOIN FETCH status.user")
  List<UserStatus> findAllWithUser();

  boolean existsByUser(User user);
}
