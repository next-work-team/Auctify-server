package org.example.auctify.dto.chat;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LastMessageDto {
	private String text;
	private LocalDateTime timestamp;
	private boolean isRead;
	private boolean isMine;
}
