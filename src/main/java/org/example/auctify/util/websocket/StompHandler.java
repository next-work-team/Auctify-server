package org.example.auctify.util.websocket;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.auctify.jwt.JWTUtil;
import org.example.auctify.service.chat.ChatRoomService;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
@Component
@RequiredArgsConstructor
public class StompHandler implements ChannelInterceptor {

	private final JWTUtil jwtUtil;
	private ChatRoomService chatRoomService;

	// 클라이언트의 상태를 저장할 맵
	private final ConcurrentHashMap<String, SessionInfo> sessionMap = new ConcurrentHashMap<>();


	//websocket을 통해 들어온 요청이 처리 되기전 실행됨
	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
		String sessionId = accessor.getSessionId();

		//websocket 연결시 헤더의 jwt token 유효성 검증
		if (StompCommand.CONNECT == accessor.getCommand()) {
			String accessToken = getAccessToken(accessor);
			Long userId = verifyAccessToken(accessToken);
			Long chatRoomId = getChatRoomId(accessor);

			log.info("AccessToken:{}, nickname:{}, chatRoomId:{}", accessToken, userId, chatRoomId);

			// 클라이언트의 상태 저장
			sessionMap.put(sessionId, new SessionInfo(chatRoomId, userId));
			connectToChatRoom(chatRoomId, userId);

		} else if (StompCommand.DISCONNECT == accessor.getCommand()) {
			SessionInfo sessionInfo = sessionMap.remove(sessionId);
			if (sessionInfo != null) {
				log.info("stompHandler에서 chatroomId : {} , nickname조회 : {}", sessionInfo.getChatRoomId(),
						sessionInfo.getUserId());
				chatRoomService.disconnectChatRoom(sessionInfo.getChatRoomId(), sessionInfo.getUserId());
			}
		} else if (StompCommand.UNSUBSCRIBE == accessor.getCommand()) {
			log.info("구독 취소");
		} else if (StompCommand.SUBSCRIBE == accessor.getCommand()) {
			log.info("구독 완료");
		}
		return message;
	}

	private void connectToChatRoom(Long chatRoomId, Long userId) {
		log.info("chatRoomId : {}", chatRoomId);
		chatRoomService.connectChatRoom(chatRoomId, userId);
		log.info("입장한 userId : {}", userId);
	}

	private String getAccessToken(StompHeaderAccessor accessor) {
//		String accessToken = accessor.getFirstNativeHeader("Authorization");
//		if (accessToken == null) {
//			throw new NullPointerException("accessToken을 찾을 수 없습니다");
//		}
//		return accessToken.substring(7);

		String cookieHeader = accessor.getFirstNativeHeader("Cookie");
		if (cookieHeader == null || cookieHeader.trim().isEmpty()) {
			throw new NullPointerException("Cookie 헤더를 찾을 수 없습니다");
		}

		// Cookie 헤더에서 Authorization 값을 추출
		String authorization = null;
		String[] cookies = cookieHeader.split(";\\s*");
		for (String cookie : cookies) {
			if (cookie.startsWith("Authorization=")) {
				authorization = cookie.substring("Authorization=".length());
				break;
			}
		}

		if (authorization == null || authorization.trim().isEmpty()) {
			throw new NullPointerException("Authorization 쿠키를 찾을 수 없습니다");
		}

		return authorization;
	}

	private Long verifyAccessToken(String accessToken) {
		return jwtUtil.getUserId(accessToken);
	}

	private Long getChatRoomId(StompHeaderAccessor accessor) {
		return Long.valueOf(Objects.requireNonNull(accessor.getFirstNativeHeader("chatRoomId")));
	}

	private static class SessionInfo {
		private final Long chatRoomId;
		private final Long userId;

		public SessionInfo(Long chatRoomId, Long userId) {
			this.chatRoomId = chatRoomId;
			this.userId = userId;
		}

		public Long getChatRoomId() {
			return chatRoomId;
		}

		public Long getUserId() {
			return userId;
		}
	}
}
