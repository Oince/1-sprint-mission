package com.sprint.mission.discodeit.dto.response;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ChannelDetailResponse(
    UUID id,
    Channel.Type type,
    String name,
    String description,
    Instant lastMessageAt,
    List<UUID> participantIds
) {

  public static ChannelDetailResponse of(Channel channel, Instant latestMessageTime) {
    List<UUID> userIds = channel.getUsers().values().stream()
        .map(User::getId)
        .toList();
    return new ChannelDetailResponse(
        channel.getId(),
        channel.getType(),
        channel.getName(),
        channel.getDescription(),
        latestMessageTime,
        userIds
    );
  }
}
