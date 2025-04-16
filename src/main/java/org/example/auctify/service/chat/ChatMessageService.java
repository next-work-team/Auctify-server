package org.example.auctify.service.chat;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.example.auctify.dto.chat.MessageDto;
import org.example.auctify.dto.notification.ChatNotificationDto;
import org.example.auctify.dto.notification.NotificationType;
import org.example.auctify.entity.chat.ChatMessageEntity;
import org.example.auctify.entity.user.UserEntity;
import org.example.auctify.repository.chat.ChatMessageRepository;
import org.example.auctify.repository.user.UserRepository;
import org.example.auctify.service.notification.NotificationSender;
import org.example.auctify.service.notification.NotificationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatMessageService {
	private final ChatMessageRepository chatMessageRepository;
	private final MessageSender messageSender;
	private final NotificationService notificationService;

	public void sendMessage(MessageDto messageDto) {
		ChatMessageEntity chatMessage = ChatMessageEntity.builder()
				.id(ObjectId.get().toHexString())
				.sendDate(LocalDateTime.now())
				.userId(messageDto.getSender())
				.chatRoomId(messageDto.getChatRoomId())
				.content(messageDto.getContent())
				.build();

		chatMessageRepository.save(chatMessage);
		messageSender.send("/sub/chat/" + messageDto.getChatRoomId(), messageDto);
		notificationService.notifyChat(messageDto);
	}
}
