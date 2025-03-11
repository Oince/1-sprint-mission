package com.sprint.mission.discodeit.dto.response;

import com.sprint.mission.discodeit.entity.Channel;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ChannelDetailResponse(
    UUID id,
    Channel.Type type,
    String name,
    String description,
    List<UserResponse> participantIds,
    Instant lastMessageAt
) {

  public static ChannelDetailResponse of(Channel channel, Instant latestMessageTime,
      List<UserResponse> participantIds) {
    return new ChannelDetailResponse(
        channel.getId(),
        channel.getType(),
        channel.getName(),
        channel.getDescription(),
        participantIds,
        latestMessageTime
    );
  }
}
