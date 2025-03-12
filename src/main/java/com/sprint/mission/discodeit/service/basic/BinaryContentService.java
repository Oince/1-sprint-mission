package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.exception.FileIOException;
import com.sprint.mission.discodeit.exception.NotFoundException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class BinaryContentService {

  private final BinaryContentRepository binaryContentRepository;
  private final BinaryContentStorage binaryContentStorage;

  @Transactional
  public BinaryContent create(MultipartFile file) {

    long size = file.getSize();
    String fileName = file.getOriginalFilename();
    String contentType = fileName.substring(fileName.lastIndexOf('.'));

    BinaryContent content = binaryContentRepository
        .save(BinaryContent.create(size, fileName, contentType));
    try {
      binaryContentStorage.put(content.getId(), file.getBytes());
    } catch (IOException e) {
      throw new FileIOException("파일 생성 실패");
    }

    return content;
  }

  @Transactional
  public List<BinaryContent> create(List<MultipartFile> files) {
    if (files == null) {
      return Collections.emptyList();
    }
    return files.stream()
        .map(this::create)
        .toList();
  }

  public BinaryContent find(UUID id) {
    return binaryContentRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("등록되지 않은 binary newContent. id=" + id));
  }

  @Transactional
  public void delete(UUID id) {
    binaryContentStorage.delete(id);
    binaryContentRepository.deleteById(id);
  }
}
