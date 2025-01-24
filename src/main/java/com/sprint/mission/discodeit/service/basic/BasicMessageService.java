package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.List;
import java.util.UUID;

public class BasicMessageService implements MessageService {

    private static final BasicMessageService basicMessageService = new BasicMessageService();

    private final ChannelRepository channelRepository;
    private final MessageRepository messageRepository;

    private BasicMessageService() {
        this.channelRepository = FileChannelRepository.getInstance();
        this.messageRepository = FileMessageRepository.getInstance();
    }

    public static BasicMessageService getInstance() {
        return basicMessageService;
    }

    @Override
    public Message createMessage(MessageDto messageDto) {
        Channel channel = channelRepository.findChannel(messageDto.getChannel().getId());
        User user = channel.getUser(messageDto.getWriter().getId());
        Message message = Message.of(user, messageDto.getContent(), channel);
        return messageRepository.save(message);
    }

    @Override
    public Message readMessage(UUID messageId) {
        return messageRepository.findMessage(messageId);
    }

    @Override
    public List<Message> readAll() {
        return messageRepository.findAll();
    }

    @Override
    public void updateMessage(UUID messageId, String content) {
        Message message = messageRepository.findMessage(messageId);
        message.updateContent(content);
        messageRepository.updateMessage(message);
    }

    @Override
    public void deleteMessage(UUID messageId) {
        messageRepository.deleteMessage(messageId);
    }
}