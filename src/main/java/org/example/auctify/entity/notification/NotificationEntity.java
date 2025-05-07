package org.example.auctify.entity.notification;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.auctify.dto.notification.NotificationType;
import org.example.auctify.entity.BaseTimeEntity;
import org.example.auctify.entity.Goods.GoodsEntity;
import org.example.auctify.entity.chat.ChatRoomEntity;
import org.example.auctify.entity.user.UserEntity;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "notification")
@Getter
public class NotificationEntity extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "receiver_id")
	private UserEntity receiver;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sender_id")
	private UserEntity sender;

	private String content;

	private NotificationType notificationType;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "room_id")
	private ChatRoomEntity chatRoom;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "goods_id")
	private GoodsEntity goods;

	private Boolean isRead;

	public void readNotification() {
		this.isRead = true;
	}
}
