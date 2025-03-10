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
import java.util.ArrayList;
import java.util.Optional;
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

    User user = userRepository.findById(userId)
        .orElseThrow(() -> new NotFoundException("등록되지 않은 user. id=" + userId));
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> new NotFoundException("등록되지 않은 channel. id=" + channelId));

    List<ReadStatus> readStatuses = readStatusRepository.findByUser(user);
    for (ReadStatus readStatus : readStatuses) {
      if (readStatus.getChannel().getId().equals(channel.getId())) {
        throw new DuplicateException("이미 존재하는 ReadStatus");
      }
    }

    return readStatusRepository.save(ReadStatus.of(user, channel));
  }

  public ReadStatus find(UUID id) {
    return readStatusRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("등록되지 않은 ReadStatus. id=" + id));
  }

  public List<ReadStatus> findAllByUserId(UUID userId) {
    Optional<User> user = userRepository.findById(userId);
    if (user.isPresent()) {
      return readStatusRepository.findByUser(user.get());
    } else {
      return new ArrayList<>();
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