package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.exception.DuplicateException;
import com.sprint.mission.discodeit.exception.NotFoundException;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReadStatusService {

  private final ReadStatusRepository readStatusRepository;
  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;

  public ReadStatus create(ReadStatusCreateRequest dto) {
    UUID userId = dto.userId();
    UUID channelId = dto.channelId();

    userRepository.findById(userId)
        .orElseThrow(() -> new NotFoundException("등록되지 않은 user. id=" + userId));
    channelRepository.findById(channelId)
        .orElseThrow(() -> new NotFoundException("등록되지 않은 channel. id=" + channelId));

    List<ReadStatus> readStatuses = readStatusRepository.findByUserId(userId);
    for (ReadStatus readStatus : readStatuses) {
      if (readStatus.getChannelId().equals(channelId)) {
        throw new DuplicateException("이미 존재하는 ReadStatus");
      }
    }

    return readStatusRepository.save(ReadStatus.of(userId, channelId));
  }

  public ReadStatus find(UUID id) {
    return readStatusRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("등록되지 않은 ReadStatus. id=" + id));
  }

  public List<ReadStatus> findAllByUserId(UUID userId) {
    return readStatusRepository.findByUserId(userId);
  }

  public void update(UUID id, Instant newLastReadAt) {
    ReadStatus readStatus = find(id);
    readStatus.updateLastReadAt(newLastReadAt);
    readStatusRepository.save(readStatus);
  }

  public void delete(UUID id) {
    readStatusRepository.delete(id);
  }
}