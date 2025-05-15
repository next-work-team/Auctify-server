package org.example.auctify.entity.chat;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;


@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@RedisHash(value = "chatRoom")
public class RedisChatRoomEntity {
	@Id
	private String id;

	@Indexed
	private Long chatRoomId;

	@Indexed
	private Long userId;
}
