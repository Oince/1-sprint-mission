package com.sprint.mission.discodeit.exception.binarycontent.file;

import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.binarycontent.BinaryContentException;
import java.util.Map;

public class FileCreateException extends BinaryContentException {

  public FileCreateException(Map<String, Object> details) {
    super(ErrorCode.FILE_CREATE_FAIL, details);
  }
}
