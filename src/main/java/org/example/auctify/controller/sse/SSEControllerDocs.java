package org.example.auctify.controller.sse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.auctify.dto.social.CustomOauth2User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Tag(name = "SSE", description = "SSE API")
public interface SSEControllerDocs {

	@Operation(summary = "알림 SSE 구독", description = "알림 SSE 구독하는 API 입니다.")
	SseEmitter subscribeNotification(
			@AuthenticationPrincipal CustomOauth2User user
	);

	@Operation(summary = "입찰 SSE 구독", description = "입찰 SSE 구독하는 API 입니다.")
	SseEmitter subscribeBid();
}
