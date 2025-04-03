package org.example.auctify.repository.chat;

import java.util.List;
import java.util.Optional;
import org.example.auctify.dto.chat.UserChatRoomStatus;
import org.example.auctify.entity.chat.ChatRoomEntity;
import org.example.auctify.entity.chat.UserChatRoomEntity;
import org.example.auctify.entity.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserChatRoomRepository extends JpaRepository<UserChatRoomEntity, Long> {
	Optional<UserChatRoomEntity> findByChatRoomAndUser(ChatRoomEntity chatRoom, UserEntity user);


	List<UserChatRoomEntity> findAllByUserAndChatRoom(UserEntity user, ChatRoomEntity chatRoom);

	List<UserChatRoomEntity> findAllByUserAndUserChatRoomStatus(UserEntity userId, UserChatRoomStatus status);
}
