package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, UUID> {

  List<Message> findByChannel_Id(UUID channelId);

  Slice<Message> findSliceByChannel_Id(UUID channelId, Pageable pageable);

  Page<Message> findPageByChannel_Id(UUID channelId, Pageable pageable);

}
