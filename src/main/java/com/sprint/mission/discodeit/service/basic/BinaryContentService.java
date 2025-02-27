package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.exception.FileIOException;
import com.sprint.mission.discodeit.exception.NotFoundException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
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
  private final UserRepository userRepository;
  private final MessageRepository messageRepository;
  private final FileManager fileManager;

  private final Path directoryPath = Path.of(System.getProperty("user.dir"), "files");

  @PostConstruct
  public void init() {
    fileManager.createDirectory(directoryPath);
  }

  public BinaryContent create(MultipartFile file) {

    UUID id = UUID.randomUUID();
    String fileName = generateFileName(id, file.getOriginalFilename());
    Path path = directoryPath.resolve(fileName);

    try {
      file.transferTo(path);
    } catch (IOException e) {
      throw new FileIOException("파일 생성 실패");
    }

    BinaryContent content = BinaryContent.of(id, fileName, path.toString());
    return binaryContentRepository.save(content);
  }

  public List<BinaryContent> create(List<MultipartFile> files) {
    return files.stream()
        .map(this::create)
        .toList();
  }

  private String generateFileName(UUID id, String originalName) {
    String extension = originalName.substring(originalName.indexOf("."));
    return id + extension;
  }

  public Path find(UUID id) {
    BinaryContent content = binaryContentRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("등록되지 않은 binary content. id=" + id));
    return Path.of(content.getPath());
  }

  public void delete(UUID id) {
    BinaryContent content = binaryContentRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("등록되지 않은 binary content. id=" + id));
    fileManager.deleteFile(Path.of(content.getPath()));
    binaryContentRepository.delete(id);
  }
}
