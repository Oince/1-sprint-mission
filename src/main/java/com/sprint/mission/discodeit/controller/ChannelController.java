package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.docs.ChannelControllerDocs;
import com.sprint.mission.discodeit.dto.request.PrivateChannelRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.response.ChannelDetailResponse;
import com.sprint.mission.discodeit.dto.response.ChannelResponse;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
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

  @PostMapping("/public")
  @Override
  public ResponseEntity<ChannelResponse> createChannel(
      @RequestBody PublicChannelRequest publicChannelRequest
  ) {
    Channel publicChannel = channelService.createPublicChannel(publicChannelRequest);
    return ResponseEntity.created(URI.create("channels/" + publicChannel.getId()))
        .body(ChannelResponse.from(publicChannel));
  }

  @PostMapping("/private")
  @Override
  public ResponseEntity<ChannelResponse> createPrivateChannel(
      @RequestBody PrivateChannelRequest privateChannelRequest
  ) {
    if (privateChannelRequest.participantIds().isEmpty()) {
      return ResponseEntity.badRequest().build();
    }
    Channel privateChannel = channelService.createPrivateChannel(
        privateChannelRequest.participantIds());
    return ResponseEntity.created(URI.create("channels/" + privateChannel.getId()))
        .body(ChannelResponse.from(privateChannel));
  }

  @GetMapping
  @Override
  public ResponseEntity<List<ChannelDetailResponse>> getChannels(@RequestParam UUID userId) {
    return ResponseEntity.ok(channelService.readAllByUserId(userId));
  }

  @PatchMapping("/{id}")
  @Override
  public ResponseEntity<ChannelResponse> updatePublicChannel(
      @PathVariable UUID id,
      @RequestBody PublicChannelUpdateRequest updateRequest
  ) {
    Channel channel = channelService.updateChannel(id, updateRequest);
    return ResponseEntity.ok().body(ChannelResponse.from(channel));
  }

  @PatchMapping("/{id}/users/{userId}")
  @Override
  public ResponseEntity<Void> addUser(@PathVariable UUID id, @PathVariable UUID userId) {
    channelService.addUser(id, userId);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/{id}/users/{userId}")
  @Override
  public ResponseEntity<Void> deleteUser(@PathVariable UUID id, @PathVariable UUID userId) {
    channelService.deleteUser(id, userId);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/{id}")
  @Override
  public ResponseEntity<Void> deleteChannel(@PathVariable UUID id) {
    channelService.deleteChannel(id);
    return ResponseEntity.noContent().build();
  }
}
