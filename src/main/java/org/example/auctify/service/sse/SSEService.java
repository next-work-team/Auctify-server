package org.example.auctify.service.sse;

import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.auctify.controller.notification.NotificationController;
import org.example.auctify.controller.sse.SSEController;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class SSEService {
	public SseEmitter subscribeNotification(Long userId) {
		SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);
		try {
			sseEmitter.send(SseEmitter.event().name("connect notification"));
			log.info("Notification SSE 연결 완료");
		} catch (IOException e) {
			e.printStackTrace();
		}

		SSEController.sseEmittersNotification.put(userId, sseEmitter);

		sseEmitter.onCompletion(() -> SSEController.sseEmittersNotification.remove(userId));
		sseEmitter.onTimeout(() -> SSEController.sseEmittersNotification.remove(userId));
		sseEmitter.onError((e) -> SSEController.sseEmittersNotification.remove(userId));

		return sseEmitter;
	}

	public SseEmitter subscribeBid(String clientId) {
		SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);
		try {
			sseEmitter.send(SseEmitter.event()
					.name("connect bid")
					.data(clientId));
			log.info("Bid SSE 연결 완료");
		} catch (IOException e) {
			e.printStackTrace();
		}

		SSEController.sseEmittersBid.put(clientId, sseEmitter);

		sseEmitter.onCompletion(() -> SSEController.sseEmittersBid.remove(clientId));
		sseEmitter.onTimeout(() -> SSEController.sseEmittersBid.remove(clientId));
		sseEmitter.onError((e) -> SSEController.sseEmittersBid.remove(clientId));

		return sseEmitter;
	}
}
