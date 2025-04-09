package org.example.auctify.controller.chat;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.auctify.dto.chat.MessageDto;
import org.example.auctify.service.chat.ChatMessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

@RequiredArgsConstructor
@Controller
@Slf4j
public class ChatMessageController {
	private final ChatMessageService chatMessageService;

	@MessageMapping("/chat")
	public ResponseEntity<Void> sendMessage(@Payload final MessageDto messageDto) {
		chatMessageService.sendMessage(messageDto);

		return ResponseEntity.ok().build();
	}
}
