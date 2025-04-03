package org.example.auctify.repository.chat;

import java.util.Optional;
import org.example.auctify.entity.chat.ChatRoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoomEntity, Long> {
	Optional<ChatRoomEntity> findByRoomHashCode(int hashCode);

}
