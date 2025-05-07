package org.example.auctify.controller.notification;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.auctify.dto.notification.NotificationListResDto;
import org.example.auctify.dto.response.ApiResponseDTO;
import org.example.auctify.dto.social.CustomOauth2User;
import org.example.auctify.service.notification.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notification")
public class NotificationController implements NotificationControllerDocs{
	private final NotificationService notificationService;

	@GetMapping
	public ResponseEntity<ApiResponseDTO<List<NotificationListResDto>>> getNotificationList(@AuthenticationPrincipal CustomOauth2User user) {

		return ResponseEntity.ok(ApiResponseDTO.success(notificationService.getNotifications(user)));
	}

	@PatchMapping("/{notificationId}")
	public ResponseEntity<ApiResponseDTO<Long>> readNotification(@PathVariable("notificationId") Long id) {
		return ResponseEntity.ok(ApiResponseDTO.success(notificationService.readNotification(id)));
	}
}
