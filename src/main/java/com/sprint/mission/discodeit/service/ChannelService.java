package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.request.PublicChannelRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import java.util.List;
import java.util.UUID;

public interface ChannelService {

  Channel createPrivateChannel(List<UUID> userIds);

  Channel createPublicChannel(PublicChannelRequest publicChannelRequest);

  Channel readChannel(UUID channelId);

  List<Channel> readAllByUserId(UUID userId);

  Channel updateChannel(UUID channelId, PublicChannelUpdateRequest updateRequest);

  void deleteChannel(UUID channelId);
}
