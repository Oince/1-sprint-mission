package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.file.FileManager;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

    private final ChannelRepository channelRepository;
    private final MessageRepository messageRepository;
    private final BinaryContentRepository binaryContentRepository;

    @Override
    public Message createMessage(MessageDto messageDto) {
        Channel channel = channelRepository.findById(messageDto.getChannel().getId());
        User user = channel.getUser(messageDto.getWriter().getId());
        Message message = Message.of(user, messageDto.getContent(), channel);
        return messageRepository.save(message);
    }

    @Override
    public Message readMessage(UUID messageId) {
        return messageRepository.findById(messageId);
    }

    @Override
    public List<Message> readAllByChannelId(UUID channelId) {
        return messageRepository.findByChannelId(channelId);
    }

    @Override
    public void updateMessage(UUID messageId, String content) {
        Message message = messageRepository.findById(messageId);
        message.updateContent(content);
        messageRepository.updateMessage(message);
    }

    @Override
    public void deleteMessage(UUID messageId) {
        List<BinaryContent> contents = binaryContentRepository.findByMessageId(messageId);
        FileManager fileManager = new FileManager("messages");
        for (BinaryContent content : contents) {
            binaryContentRepository.delete(content.getId());
            fileManager.deleteFile(content.getName());
        }
        messageRepository.deleteMessage(messageId);
    }
}