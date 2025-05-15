package org.example.auctify.dto.chat;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetOneToOneChatRoomListResDto {
	/*private String roomTitle;
	private String lastMessage;
	private LocalDateTime lastMessageTime;
	private String theOtherUserImage;
	private Long theOtherUserId;
	private Long chatRoomId;
	private Long goodsId;*/

	private long id;
	private UserDto user;
	private LastMessageDto lastMessage;
	private long unreadCount;
	private AuctionDto auction;
}

