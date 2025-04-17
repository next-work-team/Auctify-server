package org.example.auctify.repository.notification;

import java.util.List;
import org.example.auctify.dto.notification.NotificationType;
import org.example.auctify.entity.notification.NotificationEntity;
import org.example.auctify.entity.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {
	List<NotificationEntity> findBySenderAndNotificationTypeNot(UserEntity receiver, NotificationType notificationType);
}

