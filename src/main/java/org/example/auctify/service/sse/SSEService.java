package org.example.auctify.service.sse;

import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.example.auctify.controller.notification.NotificationController;
import org.example.auctify.controller.sse.SSEController;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
@RequiredArgsConstructor
@Transactional
public class SSEService {
	public SseEmitter subscribe(Long userId) {
		SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);
		try {
			sseEmitter.send(SseEmitter.event().name("connect"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		SSEController.sseEmitters.put(userId, sseEmitter);

		sseEmitter.onCompletion(() -> SSEController.sseEmitters.remove(userId));
		sseEmitter.onTimeout(() -> SSEController.sseEmitters.remove(userId));
		sseEmitter.onError((e) -> SSEController.sseEmitters.remove(userId));

		return sseEmitter;
	}
}
