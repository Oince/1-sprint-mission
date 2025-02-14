package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.exception.FileIOException;
import com.sprint.mission.discodeit.exception.NotFoundException;
import com.sprint.mission.discodeit.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class FileMessageRepository implements MessageRepository {

    private final String directory = "messages";
    private final String FILE_EXTENSION = ".ser";

    private final FileManager fileManager = new FileManager(directory);
    private final Path filePath = fileManager.getPath();

    @Override
    public Message save(Message message) {
        Path path = filePath.resolve(message.getId().toString().concat(FILE_EXTENSION));

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path.toFile()))) {
            oos.writeObject(message);
            return message;
        } catch (IOException e) {
            throw new FileIOException("message 저장 실패");
        }
    }

    @Override
    public Message findById(UUID messageId) {
        Path path = filePath.resolve(messageId.toString().concat(FILE_EXTENSION));

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path.toFile()))) {
            return (Message) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new NotFoundException("등록되지 않은 message입니다.");
        }
    }

    @Override
    public List<Message> findByChannelId(UUID channelId) {
        File[] files = filePath.toFile().listFiles();
        List<Message> messages = new ArrayList<>(100);

        for (File file : files) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                Message message = (Message) ois.readObject();
                if (message.getChannel().getId().equals(channelId)) {
                    messages.add(message);
                }
            } catch (IOException | ClassNotFoundException e) {
                throw new FileIOException("messages 읽기 실패");
            }
        }
        return messages;
    }

    @Override
    public void updateMessage(Message message) {
        save(message);
    }

    @Override
    public void deleteMessage(UUID messageId) {
        Path path = filePath.resolve(messageId.toString().concat(FILE_EXTENSION));

        if (Files.exists(path)) {
            try {
                Files.delete(path);
            } catch (IOException e) {
                throw new FileIOException("message 삭제 실패");
            }
        }
    }
}