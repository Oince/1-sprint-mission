package com.sprint.mission.discodeit.dto.response;

import java.time.Instant;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;

public record PageResponse<T>(
    List<T> content,
    Object nextCursor,
    int size,
    boolean hasNext,
    Long totalElements
) {

  public static <T> PageResponse<T> fromSlice(Slice<T> slice, Instant nextCursor) {
    return new PageResponse<>(
        slice.getContent(),
        nextCursor,
        slice.getSize(),
        slice.hasNext(),
        null
    );
  }

  public static <T> PageResponse<T> fromPage(Page<T> page, Instant nextCursor) {
    return new PageResponse<>(
        page.getContent(),
        nextCursor,
        page.getSize(),
        page.hasNext(),
        page.getTotalElements()
    );
  }
}
