package org.example.auctify.service.chat;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;

@Service
public class UserSessionService {
	// 사용자 ID -> 활성 채팅방 ID (null이면 비활성)
	private final Map<Long, Long> activeChatRooms = new ConcurrentHashMap<>();

	// 사용자가 채팅방에 입장
	public void enterChatRoom(Long userId, Long chatRoomId) {
		activeChatRooms.put(userId, chatRoomId);
	}

	// 사용자가 채팅방에서 퇴장
	public void leaveChatRoom(Long userId) {
		activeChatRooms.remove(userId);
	}

	// 사용자가 특정 채팅방에 활성 상태인지 확인
	public boolean isUserActiveInChatRoom(Long userId, Long chatRoomId) {
		return chatRoomId.equals(activeChatRooms.get(userId));
	}
}
