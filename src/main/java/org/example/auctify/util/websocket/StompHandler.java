package org.example.auctify.util.websocket;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.auctify.jwt.JWTUtil;
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

	//websocket을 통해 들어온 요청이 처리 되기전 실행됨
	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

		//websocket 연결시 헤더의 jwt token 유효성 검증
		if (StompCommand.CONNECT == accessor.getCommand()) {
			String accessToken = getAccessToken(accessor);
			String nickname = verifyAccessToken(accessToken);
		}
		return message;
	}

	private String getAccessToken(StompHeaderAccessor accessor) {
		String accessToken = accessor.getFirstNativeHeader("Authorization");
		if (accessToken == null) {
			throw new NullPointerException("accessToken을 찾을 수 없습니다");
		}
		return accessToken.substring(7);
	}

	private String verifyAccessToken(String accessToken) {
		return jwtUtil.getNickname(accessToken);
	}
}
