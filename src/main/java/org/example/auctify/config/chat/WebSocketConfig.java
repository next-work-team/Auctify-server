package org.example.auctify.config.chat;

import lombok.RequiredArgsConstructor;
import org.example.auctify.service.chat.UserSessionService;
import org.example.auctify.util.websocket.StompHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Configuration
@EnableWebSocketMessageBroker // Stomp를 사용하기 위한
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
	private final StompHandler stompHandler;
	private final UserSessionService userSessionService;


	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		registry.enableSimpleBroker("/sub"); // 메세지를 구독하는 요청 설정
		// @MessageMapping("hello") 라면 경로는 -> /pub/hello
		registry.setApplicationDestinationPrefixes("/pub"); // 메세지를 발행하는 요청 설정
	}

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		/*registry.addEndpoint("/stomp").setAllowedOrigins("*")
				.withSockJS(); // sock.js를 통하여 낮은 버전의 브라우저에서도 websocket이 동작할수 있게 설정*/
		registry.addEndpoint("/stomp")
				.setAllowedOrigins("*"); // api 통신 시, withSockJS() 설정을 빼야됨
	}

	@Override
	public void configureClientInboundChannel(ChannelRegistration registration) {
		registration.interceptors(stompHandler);
	}

	@EventListener
	public void handleWebSocketDisconnect(SessionDisconnectEvent event) {
		String userId = event.getUser() != null ? event.getUser().getName() : null;
		if (userId != null) {
			userSessionService.leaveChatRoom(Long.parseLong(userId));
		}
	}
}
