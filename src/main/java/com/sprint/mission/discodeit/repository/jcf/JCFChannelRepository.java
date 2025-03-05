package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf")
public class JCFChannelRepository implements ChannelRepository {

  private final Map<UUID, Channel> data;

  public JCFChannelRepository() {
    data = new HashMap<>(100);
  }

  @Override
  public Channel save(Channel channel) {
    data.put(channel.getId(), channel);
    return channel;
  }

  @Override
  public Optional<Channel> findById(UUID channelId) {
    return Optional.ofNullable(data.get(channelId));
  }

  @Override
  public List<Channel> findAll() {
    return new ArrayList<>(data.values());
  }

  @Override
  public void updateChannel(Channel channel) {
    data.put(channel.getId(), channel);
  }

  @Override
  public void deleteChannel(UUID channelId) {
    data.remove(channelId);
  }
}
