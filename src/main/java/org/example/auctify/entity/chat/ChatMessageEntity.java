package org.example.auctify.entity.chat;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
@Data
@Document(collection = "chatMessage")
public class ChatMessageEntity {
	@Id
	private String id;

	private Long userId;

	private Long chatRoomId;

	private String content; // 메세지 내용

	private LocalDateTime sendDate; // 보낸 시간

	private Integer unReadCount;
}
