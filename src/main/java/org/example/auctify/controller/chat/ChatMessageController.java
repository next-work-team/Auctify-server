package org.example.auctify.controller.chat;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.auctify.dto.chat.JoinRoomDto;
import org.example.auctify.dto.chat.LeaveRoomDto;
import org.example.auctify.dto.chat.MessageDto;
import org.example.auctify.service.chat.ChatMessageService;
import org.example.auctify.service.chat.UserSessionService;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
@Slf4j
public class ChatMessageController {
	private final ChatMessageService chatMessageService;
	private final UserSessionService userSessionService;

	@MessageMapping("/chat")
	public ResponseEntity<Void> sendMessage(@Payload final MessageDto messageDto) {
		chatMessageService.sendMessage(messageDto);

		return ResponseEntity.ok().build();
	}

	@MessageMapping("/chat/enter")
	public void joinRoom(@Payload JoinRoomDto joinRoomDto) {
		userSessionService.enterChatRoom(joinRoomDto.getSenderId(), joinRoomDto.getChatRoomId());
	}

	@MessageMapping("/chat/leave")
	public void leaveRoom(@Payload LeaveRoomDto leaveRoomDto) {
		userSessionService.leaveChatRoom(leaveRoomDto.getSenderId());
	}
}
