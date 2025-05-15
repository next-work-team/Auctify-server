package org.example.auctify.repository.chat;

public interface CustomChatMessageRepository {
	void updateUnReadCount(Long chatRoomId, Long userId);
}
