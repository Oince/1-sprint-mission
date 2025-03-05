package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.exception.FileIOException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import jakarta.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
public class FileBinaryContentRepository implements BinaryContentRepository {

  private final Path directoryPath;
  private final String FILE_EXTENSION = ".ser";

  private final FileManager fileManager;

  public FileBinaryContentRepository(
      @Value("${discodeit.repository.file-directory}") String directory, FileManager fileManager) {
    this.fileManager = fileManager;
    this.directoryPath = Path.of(System.getProperty("user.dir"), directory, "binary_contents");
  }

  @PostConstruct
  private void init() {
    fileManager.createDirectory(directoryPath);
  }

  @Override
  public BinaryContent save(BinaryContent content) {
    Path path = directoryPath.resolve(content.getId().toString().concat(FILE_EXTENSION));

    try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path.toFile()))) {
      oos.writeObject(content);
      return content;
    } catch (IOException e) {
      throw new FileIOException("BinaryContent 저장 실패");
    }
  }

  @Override
  public Optional<BinaryContent> findById(UUID id) {
    Path path = directoryPath.resolve(id.toString().concat(FILE_EXTENSION));

    try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path.toFile()))) {
      BinaryContent content = (BinaryContent) ois.readObject();
      return Optional.of(content);
    } catch (IOException | ClassNotFoundException e) {
      return Optional.empty();
    }
  }

  @Override
  public void delete(UUID id) {
    Path path = directoryPath.resolve(id.toString().concat(FILE_EXTENSION));

    if (Files.exists(path)) {
      try {
        Files.delete(path);
      } catch (IOException e) {
        throw new FileIOException("BinaryContent 삭제 실패");
      }
    }
  }
}
