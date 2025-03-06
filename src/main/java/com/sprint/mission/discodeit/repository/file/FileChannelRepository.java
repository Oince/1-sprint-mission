package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.exception.FileIOException;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import jakarta.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
public class FileChannelRepository implements ChannelRepository {

  private final Path directoryPath;
  private final String FILE_EXTENSION = ".ser";

  private final FileManager fileManager;

  public FileChannelRepository(@Value("${discodeit.repository.file-directory}") String directory,
      FileManager fileManager) {
    this.fileManager = fileManager;
    this.directoryPath = Path.of(System.getProperty("user.dir"), directory, "channels");
  }

  @PostConstruct
  private void init() {
    fileManager.createDirectory(directoryPath);
  }

  @Override
  public Channel save(Channel channel) {
    Path path = directoryPath.resolve(channel.getId().toString().concat(FILE_EXTENSION));

    try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path.toFile()))) {
      oos.writeObject(channel);
      return channel;
    } catch (IOException e) {
      throw new FileIOException("channel 저장 실패");
    }
  }

  @Override
  public Optional<Channel> findById(UUID channelId) {
    Path path = directoryPath.resolve(channelId.toString().concat(FILE_EXTENSION));

    try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path.toFile()))) {
      Channel channel = (Channel) ois.readObject();
      return Optional.of(channel);
    } catch (IOException | ClassNotFoundException e) {
      throw new FileIOException("channel 읽기 실패");
    }
  }

  @Override
  public List<Channel> findAll() {
    File[] files = directoryPath.toFile().listFiles();
    List<Channel> channels = new ArrayList<>();

    if (files == null) {
      return channels;
    }

    for (File file : files) {
      try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
        Channel channel = (Channel) ois.readObject();
        channels.add(channel);
      } catch (IOException | ClassNotFoundException e) {
        throw new FileIOException("channel 읽기 실패");
      }
    }

    return channels;
  }

  @Override
  public void updateChannel(Channel channel) {
    save(channel);
  }

  @Override
  public void deleteChannel(UUID channelId) {
    Path path = directoryPath.resolve(channelId.toString().concat(FILE_EXTENSION));

    if (Files.exists(path)) {
      try {
        Files.delete(path);
      } catch (IOException e) {
        throw new FileIOException("channel 삭제 실패");
      }
    }
  }
}