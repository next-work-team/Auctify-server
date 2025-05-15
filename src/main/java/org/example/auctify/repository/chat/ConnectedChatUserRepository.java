package org.example.auctify.repository.chat;

import java.util.List;
import java.util.Optional;
import org.example.auctify.entity.chat.RedisChatRoomEntity;
import org.springframework.data.repository.CrudRepository;

public interface ConnectedChatUserRepository extends CrudRepository<RedisChatRoomEntity, String> {
	List<RedisChatRoomEntity> findAllByChatRoomId(Long chatRoomId);

	Optional<RedisChatRoomEntity> findByChatRoomIdAndUserId(Long chatRoomId, Long userId);
}
