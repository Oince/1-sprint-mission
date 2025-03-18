package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.Duration;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_statuses")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserStatus extends BaseUpdatableEntity {

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", unique = true)
  private User user;

  @Column(nullable = false)
  private Instant lastActiveAt;

  private UserStatus(User user) {
    this.user = user;
    this.lastActiveAt = Instant.now();
    user.setUserStatus(this);
  }

  public static UserStatus create(User user) {
    return new UserStatus(user);
  }

  public boolean isOnline() {
    long minutes = Duration.between(this.getLastActiveAt(), Instant.now()).toMinutes();
    return minutes <= 5;
  }

  public void updateLastActiveAt(Instant lastActiveAt) {
    this.lastActiveAt = lastActiveAt;
  }
}
