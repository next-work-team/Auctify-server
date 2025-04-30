package org.example.auctify.controller.sse;

import java.util.Map;
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
	public static Map<Long, SseEmitter> sseEmitters = new ConcurrentHashMap<>();

	@GetMapping("/subscribe")
	public SseEmitter subscribe(@AuthenticationPrincipal CustomOauth2User user) {
		Long userId = user.getUserId();
		SseEmitter sseEmitter = sseService.subscribe(userId);

		return sseEmitter;
	}
}
