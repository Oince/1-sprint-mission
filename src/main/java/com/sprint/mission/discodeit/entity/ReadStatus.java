package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "read_statuses", uniqueConstraints = {
    @UniqueConstraint(
        name = "read_statuses_user_id_channel_id_key",
        columnNames = {
            "user_id", "channel_id"
        }
    )
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReadStatus extends BaseUpdatableEntity {

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "channel_id")
  private Channel channel;

  @Column(nullable = false)
  private Instant lastReadAt;

  @Builder(access = AccessLevel.PRIVATE)
  private ReadStatus(User user, Channel channel) {
    this.user = user;
    this.channel = channel;
    this.lastReadAt = Instant.now();
  }

  public static ReadStatus create(User user, Channel channel) {
    return ReadStatus.builder()
        .user(user)
        .channel(channel)
        .build();
  }

  public void updateLastReadAt(Instant lastReadAt) {
    this.lastReadAt = lastReadAt;
  }
}
