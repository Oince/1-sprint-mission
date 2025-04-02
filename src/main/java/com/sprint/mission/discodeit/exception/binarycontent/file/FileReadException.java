package com.sprint.mission.discodeit.exception.binarycontent.file;

import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.binarycontent.BinaryContentException;
import java.util.Map;

public class FileReadException extends BinaryContentException {

  public FileReadException(Map<String, Object> details) {
    super(ErrorCode.FILE_READ_FAIL, details);
  }
}
