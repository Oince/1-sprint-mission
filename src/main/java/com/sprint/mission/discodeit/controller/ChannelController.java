package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.docs.ChannelControllerDocs;
import com.sprint.mission.discodeit.dto.request.PrivateChannelRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.response.ChannelResponse;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.service.ChannelService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/channels")
public class ChannelController implements ChannelControllerDocs {

  private final ChannelService channelService;
  private final ChannelMapper channelMapper;

  @PostMapping("/public")
  @Override
  public ResponseEntity<ChannelResponse> createPublicChannel(
      @RequestBody PublicChannelRequest publicChannelRequest
  ) {
    ChannelResponse publicChannel = channelService.createPublicChannel(publicChannelRequest);
    return ResponseEntity.status(HttpStatus.CREATED).body(publicChannel);
  }

  @PostMapping("/private")
  @Override
  public ResponseEntity<ChannelResponse> createPrivateChannel(
      @RequestBody PrivateChannelRequest privateChannelRequest
  ) {
    if (privateChannelRequest.participantIds().isEmpty()) {
      return ResponseEntity.badRequest().build();
    }
    ChannelResponse privateChannel = channelService.
        createPrivateChannel(privateChannelRequest.participantIds());
    return ResponseEntity.status(HttpStatus.CREATED).body(privateChannel);
  }

  @GetMapping
  @Override
  public ResponseEntity<List<ChannelResponse>> getChannels(@RequestParam UUID userId) {
    return ResponseEntity.ok(channelService.readAllByUserId(userId));
  }

  @PatchMapping("/{id}")
  @Override
  public ResponseEntity<ChannelResponse> updatePublicChannel(
      @PathVariable UUID id,
      @RequestBody PublicChannelUpdateRequest updateRequest
  ) {
    ChannelResponse channel = channelService.updateChannel(id, updateRequest);
    return ResponseEntity.ok().body(channel);
  }

  @DeleteMapping("/{id}")
  @Override
  public ResponseEntity<Void> deleteChannel(@PathVariable UUID id) {
    channelService.deleteChannel(id);
    return ResponseEntity.noContent().build();
  }
}
