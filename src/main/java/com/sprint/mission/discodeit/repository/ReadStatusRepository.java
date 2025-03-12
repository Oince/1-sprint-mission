package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReadStatusRepository extends JpaRepository<ReadStatus, UUID> {

  List<ReadStatus> findByUser_Id(UUID userId);

  List<ReadStatus> findByChannel(Channel channel);

  void deleteByChannel(Channel channel);

  boolean existsByUser_IdAndChannel_Id(UUID userId, UUID channelId);
}
