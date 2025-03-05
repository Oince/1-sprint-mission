package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.request.PublicChannelRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.response.ChannelDetailResponse;
import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelService {

  Channel createPrivateChannel(List<UUID> userIds);

  Channel createPublicChannel(PublicChannelRequest publicChannelRequest);

  ChannelDetailResponse readChannel(UUID channelId);

  List<ChannelDetailResponse> readAllByUserId(UUID userId);

  Channel updateChannel(UUID channelId, PublicChannelUpdateRequest updateRequest);

  void addUser(UUID channelId, UUID userId);

  void deleteUser(UUID channelId, UUID userId);

  void deleteChannel(UUID channelId);
}
