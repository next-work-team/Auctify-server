package org.example.auctify.service.notification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.auctify.dto.chat.MessageDto;
import org.example.auctify.dto.notification.ChatNotificationDto;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationSender {
	private final SimpMessageSendingOperations simpMessageSendingOperations;

	public void send(final String destination, final ChatNotificationDto notification) {
		simpMessageSendingOperations.convertAndSend(destination, notification);
	}
}

