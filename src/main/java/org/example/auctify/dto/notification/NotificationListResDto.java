package org.example.auctify.dto.notification;

import java.time.Duration;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationListResDto {
	private Long id;
	private Long goodsId;
	private String goodsName;
	private String sender;
	private NotificationType notificationType;
	private LocalDateTime createdAt;
	private Long price;
	private Duration endTime;
}
