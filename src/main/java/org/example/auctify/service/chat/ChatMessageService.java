package org.example.auctify.service.chat;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.example.auctify.dto.chat.EnterMessageDto;
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
@Slf4j
public class ChatMessageService {
	private final ChatMessageRepository chatMessageRepository;
	private final MessageSender messageSender;
	private final NotificationService notificationService;
	private final ChatRoomService chatRoomService;
	private final SendEnterMessage sendEnterMessage;

	public void sendMessage(MessageDto messageDto) {
		Integer readCount = chatRoomService.checkConnectedUser(messageDto.getChatRoomId());
		log.info("현재 채팅방에 접속해있는 인원 : {}", readCount);

		ChatMessageEntity chatMessage = ChatMessageEntity.builder()
				.id(ObjectId.get().toHexString())
				.sendDate(LocalDateTime.now())
				.userId(messageDto.getSender())
				.chatRoomId(messageDto.getChatRoomId())
				.content(messageDto.getContent())
				.unReadCount(2-readCount)
				.build();

		chatMessageRepository.save(chatMessage);
		messageDto.setReadCount(2 - readCount);
		messageSender.send("/sub/chat/" + messageDto.getChatRoomId(), messageDto);
		//notificationService.notifyChat(messageDto);
	}

	public void sendEnterMessage(EnterMessageDto enterMessageDto) {
		sendEnterMessage.sendEnterMessage(enterMessageDto);
	}
}
