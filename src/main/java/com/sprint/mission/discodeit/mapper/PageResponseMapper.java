package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.response.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

@Component
public class PageResponseMapper {

  public <T> PageResponse<T> fromSlice(Slice<T> slice) {
    return PageResponse.fromSlice(slice);
  }

  public <T> PageResponse<T> fromPage(Page<T> page) {
    return PageResponse.fromPage(page);
  }

}
