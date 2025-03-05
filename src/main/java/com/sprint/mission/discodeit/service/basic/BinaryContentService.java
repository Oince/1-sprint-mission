package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.exception.FileIOException;
import com.sprint.mission.discodeit.exception.NotFoundException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.file.FileManager;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class BinaryContentService {

  private final BinaryContentRepository binaryContentRepository;
  private final FileManager fileManager;

  private final Path directoryPath = Path.of(System.getProperty("user.dir"), "files");

  @PostConstruct
  public void init() {
    fileManager.createDirectory(directoryPath);
  }

  public BinaryContent create(MultipartFile file) {

    UUID id = UUID.randomUUID();
    long size = file.getSize();
    String originalFilename = file.getOriginalFilename();
    String contentType = originalFilename.substring(originalFilename.lastIndexOf('.'));
    String fileName = id + contentType;
    Path path = directoryPath.resolve(fileName);

    try {
      file.transferTo(path);
    } catch (IOException e) {
      throw new FileIOException("파일 생성 실패");
    }

    BinaryContent content = BinaryContent.of(id, size, fileName, contentType, path.toString());
    return binaryContentRepository.save(content);
  }

  public List<BinaryContent> create(List<MultipartFile> files) {
    if (files == null) {
      return List.of();
    }
    return files.stream()
        .map(this::create)
        .toList();
  }

  public BinaryContent find(UUID id) {
    return binaryContentRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("등록되지 않은 binary newContent. id=" + id));
  }

  public void delete(UUID id) {
    BinaryContent content = binaryContentRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("등록되지 않은 binary newContent. id=" + id));
    fileManager.deleteFile(Path.of(content.getPath()));
    binaryContentRepository.delete(id);
  }
}
