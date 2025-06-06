package org.example.auctify.controller.notification;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.example.auctify.dto.notification.NotificationListResDto;
import org.example.auctify.dto.response.ApiResponseDTO;
import org.example.auctify.dto.social.CustomOauth2User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "알림", description = "알림 API")
public interface NotificationControllerDocs {

	@Operation(summary = "알림 목록 조회", description = "알림 목록 조회 페이지 입니다.")
	ResponseEntity<ApiResponseDTO<List<NotificationListResDto>>> getNotificationList(
			@AuthenticationPrincipal CustomOauth2User user
	);


	@Operation(summary = "알림 읽음 처리", description = "알림 읽음 처리하는 API입니다.")
	ResponseEntity<ApiResponseDTO<Long>> readNotification(
			@PathVariable("notificationId") Long id
	);
}
