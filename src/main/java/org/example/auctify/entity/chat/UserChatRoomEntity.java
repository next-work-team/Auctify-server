package org.example.auctify.entity.chat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.auctify.dto.chat.UserChatRoomStatus;
import org.example.auctify.entity.user.UserEntity;


@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Builder
public class UserChatRoomEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_chat_room_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "chat_room_id")
	private ChatRoomEntity chatRoom;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private UserEntity user;

	@Enumerated(EnumType.STRING)
	private UserChatRoomStatus userChatRoomStatus; // ACTIVE, INACTIVE ( 사용자가 방에 있는 상태, 방을 떠난 상태 )

	private LocalDateTime lastExitedAt; // 마지막에 방을 나간 시간

	// 채팅방 나가기
	public UserChatRoomEntity inActive() {
		this.userChatRoomStatus = UserChatRoomStatus.INACTIVE;
		this.lastExitedAt = LocalDateTime.now();
		return this;
	}

	public UserChatRoomEntity active() {
		this.userChatRoomStatus = UserChatRoomStatus.ACTIVE;
		return this;
	}
}
