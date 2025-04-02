package com.sprint.mission.discodeit.exception.binarycontent.file;

import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.binarycontent.BinaryContentException;
import java.util.Map;

public class DirectoryCreateException extends BinaryContentException {

  public DirectoryCreateException(Map<String, Object> details) {
    super(ErrorCode.DIRECTORY_CREATE_FAIL, details);
  }
}
