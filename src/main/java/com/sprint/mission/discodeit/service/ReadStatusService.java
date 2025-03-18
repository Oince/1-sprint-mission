package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.response.ReadStatusResponse;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.DuplicateException;
import com.sprint.mission.discodeit.exception.NotFoundException;
import com.sprint.mission.discodeit.mapper.ReadStatusMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReadStatusService {

  private final ReadStatusRepository readStatusRepository;
  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;
  private final ReadStatusMapper readStatusMapper;

  @Transactional
  public ReadStatusResponse create(ReadStatusCreateRequest dto) {
    UUID userId = dto.userId();
    UUID channelId = dto.channelId();

    User user = userRepository.findById(userId)
        .orElseThrow(() -> new NotFoundException("등록되지 않은 user. id=" + userId));
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> new NotFoundException("등록되지 않은 channel. id=" + channelId));

    if (readStatusRepository.existsByUser_IdAndChannel_Id(userId, channelId)) {
      throw new DuplicateException("이미 존재하는 ReadStatus");
    }
    ReadStatus readStatus = readStatusRepository.save(ReadStatus.create(user, channel));
    return readStatusMapper.toDto(readStatus);
  }

  private ReadStatus findById(UUID id) {
    return readStatusRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("등록되지 않은 ReadStatus. id=" + id));
  }

  public List<ReadStatusResponse> findAllByUserId(UUID userId) {
    if (!userRepository.existsById(userId)) {
      return Collections.emptyList();
    } else {
      return readStatusRepository.findByUser_Id(userId).stream()
          .map(readStatusMapper::toDto)
          .toList();
    }
  }

  @Transactional
  public ReadStatusResponse update(UUID id, Instant newLastReadAt) {
    ReadStatus readStatus = findById(id);
    readStatus.updateLastReadAt(newLastReadAt);
    readStatusRepository.save(readStatus);
    return readStatusMapper.toDto(readStatus);
  }

  @Transactional
  public void delete(UUID id) {
    readStatusRepository.deleteById(id);
  }
}