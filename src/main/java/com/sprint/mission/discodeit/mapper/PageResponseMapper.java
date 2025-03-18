package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.response.PageResponse;
import java.time.Instant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

@Component
public class PageResponseMapper {

  public <T> PageResponse<T> fromSlice(Slice<T> slice, Instant nextCursor) {
    return PageResponse.fromSlice(slice, nextCursor);
  }

  public <T> PageResponse<T> fromPage(Page<T> page, Instant nextCursor) {
    return PageResponse.fromPage(page, nextCursor);
  }

}
