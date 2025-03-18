package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.response.BinaryContentResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

public interface BinaryContentStorage {

  UUID put(UUID id, byte[] data) throws IOException;

  InputStream get(UUID id);

  void delete(UUID id);

  ResponseEntity<Resource> download(BinaryContentResponse response);
}
