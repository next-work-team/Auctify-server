package org.example.auctify.controller.sse;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import org.example.auctify.dto.social.CustomOauth2User;
import org.example.auctify.service.sse.SSEService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/sse")
@RequiredArgsConstructor
public class SSEController implements SSEControllerDocs{
	private final SSEService sseService;
	public static Map<Long, SseEmitter> sseEmittersNotification = new ConcurrentHashMap<>();
	public static Map<String, SseEmitter> sseEmittersBid = new ConcurrentHashMap<>();


	@GetMapping("/subscribe/notification")
	public SseEmitter subscribeNotification(@AuthenticationPrincipal CustomOauth2User user) {

		System.out.println("~~~ Notification SSE 요청 들어옴  ~~~");
		Long userId = user.getUserId();
		SseEmitter sseEmitter = sseService.subscribeNotification(userId);

		return sseEmitter;
	}

	@GetMapping("/subscribe/bid")
	public SseEmitter subscribeBid() {

		System.out.println("~~~ Bid SSE 요청 들어옴  ~~~");
		String clientId = UUID.randomUUID().toString();
		SseEmitter sseEmitter = sseService.subscribeBid(clientId);

		return sseEmitter;
	}
}
