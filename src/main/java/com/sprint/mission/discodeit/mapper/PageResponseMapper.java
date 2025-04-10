package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.response.MessageResponse;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import java.time.Instant;
import java.util.List;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

@Component
public class PageResponseMapper {

  public PageResponse<MessageResponse> fromMessageResponse(Slice<MessageResponse> slice) {
    List<MessageResponse> content = slice.getContent();
    Instant nextCursor;
    if (content.isEmpty()) {
      nextCursor = Instant.now();
    } else {
      nextCursor = content.get(content.size() - 1).createdAt();
    }
    return PageResponse.fromSlice(slice, nextCursor);
  }
}
