package org.example.auctify.dto.notification;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder
public class ChatNotificationDto {
	private Long roomId;
	private NotificationType notificationType;
	private String sender;
	private String content;
	private LocalDateTime date;
}
