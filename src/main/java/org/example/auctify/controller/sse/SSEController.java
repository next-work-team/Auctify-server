package org.example.auctify.controller.sse;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.auctify.dto.social.CustomOauth2User;
import org.example.auctify.jwt.JWTUtil;
import org.example.auctify.service.sse.SSEService;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/sse")
@RequiredArgsConstructor
@Slf4j
public class SSEController implements SSEControllerDocs{
	private final SSEService sseService;
	private final JWTUtil jwtUtil;

	public static Map<Long, SseEmitter> sseEmittersNotification = new ConcurrentHashMap<>();
	public static Map<String, SseEmitter> sseEmittersBid = new ConcurrentHashMap<>();


	@GetMapping(value = "/subscribe/notification", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	//public SseEmitter subscribeNotification(@AuthenticationPrincipal CustomOauth2User user) {
		public SseEmitter subscribeNotification(HttpServletRequest request) {
			String token = null;
			for (Cookie cookie : request.getCookies()) {
				if (cookie.getName().equals("Authorization")) {
					token = cookie.getValue();
					break;
				}
			}

			if (token == null || jwtUtil.isExpired(token)) {
				throw new RuntimeException("Unauthorized");
			}

			Long userId = jwtUtil.getUserId(token);
			log.info("~~~ Notification SSE 요청: userId={} ~~~", userId);
			return sseService.subscribeNotification(userId);
		}

	@GetMapping("/subscribe/bid")
	public SseEmitter subscribeBid() {
		log.info("~~~ Bid SSE 요청 들어옴  ~~~");
		String clientId = UUID.randomUUID().toString();
		SseEmitter sseEmitter = sseService.subscribeBid(clientId);

		return sseEmitter;
	}
}
