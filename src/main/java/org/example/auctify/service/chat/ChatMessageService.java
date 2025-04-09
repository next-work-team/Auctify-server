package org.example.auctify.service.chat;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.example.auctify.dto.chat.MessageDto;
import org.example.auctify.entity.chat.ChatMessageEntity;
import org.example.auctify.repository.chat.ChatMessageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatMessageService {
	private final ChatMessageRepository chatMessageRepository;
	private final MessageSender messageSender;

	public void sendMessage(MessageDto messageDto) {
		ChatMessageEntity chatMessage = ChatMessageEntity.builder()
				.id(ObjectId.get().toHexString())
				.sendDate(LocalDateTime.now())
				.userId(messageDto.getSenderId())
				.chatRoomId(messageDto.getChatRoomId())
				.content(messageDto.getContent())
				.build();

		chatMessageRepository.save(chatMessage);
		messageSender.send("/sub/chat/" + messageDto.getChatRoomId(), messageDto);
	}
}
