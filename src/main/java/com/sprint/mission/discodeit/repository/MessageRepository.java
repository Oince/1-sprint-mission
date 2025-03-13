package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MessageRepository extends JpaRepository<Message, UUID> {

  @Query("SELECT m from Message m "
      + "LEFT JOIN FETCH m.attachments "
      + "WHERE m.id = :id ")
  Optional<Message> findByIdWithAttachments(@Param("id") UUID id);

  @Query("SELECT m FROM Message m "
      + "LEFT JOIN FETCH m.attachments "
      + "WHERE m.channel.id = :channelId")
  List<Message> findByChannel_IdWithAttachments(@Param("channelId") UUID channelId);

  List<Message> findByChannel_Id(UUID channelId);

  @Query("SELECT m FROM Message m "
      + "JOIN FETCH m.channel "
      + "JOIN FETCH m.author "
      + "LEFT JOIN FETCH m.attachments "
      + "WHERE m.channel.id = :channelId ")
  Page<Message> findPageByChannel_Id(@Param("channelId") UUID channelId, Pageable pageable);

}
