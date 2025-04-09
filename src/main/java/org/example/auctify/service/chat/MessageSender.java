package org.example.auctify.service.chat;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.auctify.dto.chat.MessageDto;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageSender {
	private final SimpMessageSendingOperations simpMessageSendingOperations;

	public void send(final String destination, final MessageDto message) {
		simpMessageSendingOperations.convertAndSend(destination, message);
	}
}
