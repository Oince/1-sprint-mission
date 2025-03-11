package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.DuplicateException;
import com.sprint.mission.discodeit.exception.NotFoundException;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReadStatusService {

  private final ReadStatusRepository readStatusRepository;
  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;

  public ReadStatus create(ReadStatusCreateRequest dto) {
    UUID userId = dto.userId();
    UUID channelId = dto.channelId();

    User user = userRepository.findById(userId)
        .orElseThrow(() -> new NotFoundException("등록되지 않은 user. id=" + userId));
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> new NotFoundException("등록되지 않은 channel. id=" + channelId));

    Optional<ReadStatus> readStatus = readStatusRepository.findByUserAndChannel(user, channel);
    if (readStatus.isPresent()) {
      throw new DuplicateException("이미 존재하는 ReadStatus");
    }

    return readStatusRepository.save(ReadStatus.create(user, channel));
  }

  public ReadStatus find(UUID id) {
    return readStatusRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("등록되지 않은 ReadStatus. id=" + id));
  }

  public List<ReadStatus> findAllByUserId(UUID userId) {
    if (!userRepository.existsById(userId)) {
      return Collections.emptyList();
    } else {
      return readStatusRepository.findByUser_Id(userId);
    }
  }

  public void update(UUID id, Instant newLastReadAt) {
    ReadStatus readStatus = find(id);
    readStatus.updateLastReadAt(newLastReadAt);
    readStatusRepository.save(readStatus);
  }

  public void delete(UUID id) {
    readStatusRepository.deleteById(id);
  }
}