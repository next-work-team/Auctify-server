package org.example.auctify.controller.notification;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import org.example.auctify.dto.notification.NotificationListResDto;
import org.example.auctify.dto.response.ApiResponseDTO;
import org.example.auctify.dto.social.CustomOauth2User;
import org.example.auctify.entity.user.UserEntity;
import org.example.auctify.service.notification.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
public class NotificationController {
	private final NotificationService notificationService;
	public static Map<Long, SseEmitter> sseEmitters = new ConcurrentHashMap<>();

	@GetMapping("/notification/subscribe")
	public SseEmitter subscribe(@AuthenticationPrincipal CustomOauth2User user) {
		Long userId = user.getUserId();
		SseEmitter sseEmitter = notificationService.subscribe(userId);

		return sseEmitter;
	}

	@GetMapping("/notifications")
	public ResponseEntity<ApiResponseDTO<List<NotificationListResDto>>> getNotificationList(@AuthenticationPrincipal CustomOauth2User user) {

		return ResponseEntity.ok(ApiResponseDTO.success(notificationService.getNotifications(user)));
	}
}
