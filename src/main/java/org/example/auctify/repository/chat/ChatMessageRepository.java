package org.example.auctify.repository.chat;

import java.util.List;
import org.example.auctify.entity.chat.ChatMessageEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMessageRepository extends MongoRepository<ChatMessageEntity, String> {
	List<ChatMessageEntity> findByChatRoomIdAndUserIdNotOrderBySendDateAsc(Long chatRoomId, Long userId);

	Page<ChatMessageEntity> findByChatRoomIdOrderBySendDateDesc(Long chatRoomId, Pageable pageable);
}
