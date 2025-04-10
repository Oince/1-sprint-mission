package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
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
  List<Message> findByChannelIdWithAttachments(@Param("channelId") UUID channelId);

  List<Message> findByChannel_Id(UUID channelId);

  @Query("SELECT m FROM Message m "
      + "JOIN FETCH m.channel "
      + "JOIN FETCH m.author "
      + "LEFT JOIN FETCH m.attachments "
      + "WHERE m.channel.id = :channelId "
      + "AND m.createdAt < :cursor "
      + "ORDER BY m.createdAt DESC ")
  Slice<Message> findPageByChannelIdWithCursor(
      @Param("channelId") UUID channelId,
      @Param("cursor") Instant cursor,
      Pageable pageable
  );

  @Query("SELECT m FROM Message m "
      + "JOIN FETCH m.channel "
      + "JOIN FETCH m.author "
      + "LEFT JOIN FETCH m.attachments "
      + "WHERE m.channel.id = :channelId ")
  Slice<Message> findPageByChannelId(
      @Param("channelId") UUID channelId,
      Pageable pageable
  );
}
