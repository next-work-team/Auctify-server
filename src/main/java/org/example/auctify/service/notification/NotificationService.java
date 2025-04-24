package org.example.auctify.service.notification;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.example.auctify.controller.notification.NotificationController;
import org.example.auctify.dto.chat.MessageDto;
import org.example.auctify.dto.notification.ChatNotificationDto;
import org.example.auctify.dto.notification.NotificationListResDto;
import org.example.auctify.dto.notification.NotificationType;
import org.example.auctify.dto.social.CustomOauth2User;
import org.example.auctify.entity.Goods.GoodsEntity;
import org.example.auctify.entity.bidHistory.BidHistoryEntity;
import org.example.auctify.entity.chat.ChatRoomEntity;
import org.example.auctify.entity.notification.NotificationEntity;
import org.example.auctify.entity.user.UserEntity;
import org.example.auctify.repository.bidHistory.BidHistoryRepository;
import org.example.auctify.repository.chat.ChatRoomRepository;
import org.example.auctify.repository.goods.GoodsRepository;
import org.example.auctify.repository.notification.NotificationRepository;
import org.example.auctify.repository.user.UserRepository;
import org.example.auctify.service.chat.UserSessionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationService {
	private final NotificationRepository notificationRepository;
	private final GoodsRepository goodsRepository;
	private final BidHistoryRepository bidHistoryRepository;
	private final UserRepository userRepository;
	private final NotificationSender notificationSender;
	private final UserSessionService userSessionService;
	private final ChatRoomRepository chatRoomRepository;

	public SseEmitter subscribe(Long userId) {
		SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);
		try {
			sseEmitter.send(SseEmitter.event().name("connect"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		NotificationController.sseEmitters.put(userId, sseEmitter);

		sseEmitter.onCompletion(() -> NotificationController.sseEmitters.remove(userId));
		sseEmitter.onTimeout(() -> NotificationController.sseEmitters.remove(userId));
		sseEmitter.onError((e) -> NotificationController.sseEmitters.remove(userId));

		return sseEmitter;
	}

	//채팅 알림
	public void notifyChat(MessageDto messageDto) {
		UserEntity findSender = userRepository.findById(messageDto.getSender())
				.orElseThrow(() -> new NullPointerException("존재하지 않는 회원입니다."));
		UserEntity findReceiver = userRepository.findById(messageDto.getReceiver())
				.orElseThrow(() -> new NullPointerException("존재하지 않는 회원입니다."));
		ChatRoomEntity findChatRoom = chatRoomRepository.findById(messageDto.getChatRoomId())
				.orElseThrow(() -> new NullPointerException("존재하지 않는 채팅방입니다."));

		if (!userSessionService.isUserActiveInChatRoom(findReceiver.getUserId(), messageDto.getChatRoomId())) {
			ChatNotificationDto chatNotificationDto = ChatNotificationDto.builder()
					.notificationType(NotificationType.CHAT)
					.date(LocalDateTime.now())
					.roomId(messageDto.getChatRoomId())
					.sender(findSender.getNickName())
					.content(messageDto.getContent())
					.build();

			NotificationEntity notification = NotificationEntity.builder()
					.notificationType(NotificationType.CHAT)
					.sender(findSender)
					.content(messageDto.getContent())
					.receiver(findReceiver)
					.chatRoom(findChatRoom)
					.build();
			notificationRepository.save(notification);

			notificationSender.send("/sub/notification/" + findReceiver.getUserId(), chatNotificationDto);
		}
	}

	//입찰 알림
	public void notifyBid(Long goodsId) {
		GoodsEntity goods = goodsRepository.findById(goodsId).orElseThrow(
				() -> new IllegalArgumentException("상품을 찾을 수 없습니다.")
		);

		BidHistoryEntity findBidHistory = bidHistoryRepository.findFirstByGoodsOrderByCreatedAt(goods)
				.orElseThrow(() -> new NullPointerException("입찰 내역을 찾을 수 없습니다."));

		List<UserEntity> users = goods.getBidHistories().stream()
				.filter(bidHistory -> !bidHistory.equals(findBidHistory)) // findBidHistory 제외
				.map(BidHistoryEntity::getUser)
				.collect(Collectors.toCollection(ArrayList::new));
		users.add(goods.getUser());

		for (UserEntity user : users) {
			Long userId = user.getUserId();

			if (NotificationController.sseEmitters.containsKey(userId)) {
				SseEmitter sseEmitter = NotificationController.sseEmitters.get(userId);
				try {
					Map<String, String> eventData = new HashMap<>();
					eventData.put("message", "입찰을 했습니다.");
					eventData.put("sender", findBidHistory.getUser().getNickName());        // 입찰자
					eventData.put("createdAt", findBidHistory.getCreatedAt().toString());   // 입찰 등록 시간
					eventData.put("goods", findBidHistory.getGoods().getGoodsName());    // 입찰 상품

					sseEmitter.send(SseEmitter.event().name("addBid").data(eventData));

					// DB 저장
					NotificationEntity notification = NotificationEntity.builder()
							.notificationType(NotificationType.BID)
							.sender(findBidHistory.getUser())
							.content(goods.getGoodsName())
							.receiver(user)
							.goods(goods)
							.build();
					notificationRepository.save(notification);
				} catch (Exception e) {
					NotificationController.sseEmitters.remove(userId);
				}
			}
		}
	}

	//낙찰 알림
	public void notifySuccessfulBid(Long goodsId) {
		GoodsEntity goods = goodsRepository.findById(goodsId).orElseThrow(
				() -> new IllegalArgumentException("상품을 찾을 수 없습니다.")
		);

		BidHistoryEntity successfulBid = bidHistoryRepository.findByGoodsAndBidStatus(goods, true)
				.orElseThrow(() -> new IllegalArgumentException("낙찰자가 없습니다."));

		Long userId = successfulBid.getUser().getUserId();

		if (NotificationController.sseEmitters.containsKey(userId)) {
			SseEmitter sseEmitter = NotificationController.sseEmitters.get(userId);
			try {
				Map<String, String> eventData = new HashMap<>();
				eventData.put("message", "낙찰에 성공했습니다.");
				eventData.put("goods", goods.getGoodsName()); //낙찰 성공 상품
				sseEmitter.send(SseEmitter.event().name("addSuccessfulBid").data(eventData));

				// DB 저장
				NotificationEntity notification = NotificationEntity.builder()
						.notificationType(NotificationType.SUCCESSFUL_BID)
						.content(goods.getGoodsName())
						.goods(goods)
						.build();
				notificationRepository.save(notification);
			} catch (Exception e) {
				NotificationController.sseEmitters.remove(userId);
			}
		}
	}

	//낙찰 실패 알림
	public void notifyFailBid(Long goodsId) {
		GoodsEntity goods = goodsRepository.findById(goodsId).orElseThrow(
				() -> new IllegalArgumentException("상품을 찾을 수 없습니다.")
		);

		List<UserEntity> users = goods.getBidHistories().stream()
				.filter(bidHistory -> !bidHistory.getBidStatus()) // bidStatus가 false인 경우만 필터링
				.map(BidHistoryEntity::getUser)
				.collect(Collectors.toCollection(ArrayList::new));

		for (UserEntity user : users) {
			Long userId = user.getUserId();

			if (NotificationController.sseEmitters.containsKey(userId)) {
				SseEmitter sseEmitter = NotificationController.sseEmitters.get(userId);
				try {
					Map<String, String> eventData = new HashMap<>();
					eventData.put("message", "낙찰에 실패했습니다.");
					eventData.put("goods", goods.getGoodsName()); //낙찰 실패 상품

					sseEmitter.send(SseEmitter.event().name("addFailBid").data(eventData));

					// DB 저장
					NotificationEntity notification = NotificationEntity.builder()
							.notificationType(NotificationType.FAIL_BID)
							.content(goods.getGoodsName())
							.receiver(user)
							.goods(goods)
							.build();
					notificationRepository.save(notification);
				} catch (Exception e) {
					NotificationController.sseEmitters.remove(userId);
				}
			}
		}
	}

	//입찰 내역 업데이트 시 클라이언트로 전송하는 메서드
	public void sendBidUpdate(Long goodsId) {
		GoodsEntity goods = goodsRepository.findById(goodsId).orElseThrow(
				() -> new IllegalArgumentException("상품을 찾을 수 없습니다.")
		);

		BidHistoryEntity findBidHistory = bidHistoryRepository.findFirstByGoodsOrderByCreatedAt(goods)
				.orElseThrow(() -> new NullPointerException("입찰 내역을 찾을 수 없습니다."));

		for (Long key : NotificationController.sseEmitters.keySet()) {
			if (NotificationController.sseEmitters.containsKey(key)) {
				SseEmitter sseEmitter = NotificationController.sseEmitters.get(key);

				try {
					Map<String, String> eventData = new HashMap<>();
					eventData.put("message", "입찰을 했습니다.");
					eventData.put("goodsId", goods.getGoodsId().toString()); //입찰 상품 id
					eventData.put("goodsName", goods.getGoodsName()); //입찰 상품 이름
					eventData.put("bidUser", findBidHistory.getUser().getNickName()); //입찰자 이름
					eventData.put("bidPrice", findBidHistory.getBidPrice().toString()); //입찰 가격
					eventData.put("createdAt", findBidHistory.getCreatedAt().toString()); //입찰 가격

					sseEmitter.send(SseEmitter.event().name("updateBid").data(eventData));
				} catch (Exception e) {
					NotificationController.sseEmitters.remove(key);
				}
			}
		}
	}

	public void notifyDeadline() {
		List<GoodsEntity> goodsList = goodsRepository.findDeadlineBiddingGoods();

		for (GoodsEntity goods : goodsList) {
			List<BidHistoryEntity> bidHistoryList = bidHistoryRepository.findByGoods(goods);

			for (BidHistoryEntity bidHistory : bidHistoryList) {
				Long userId = bidHistory.getUser().getUserId();

				if (NotificationController.sseEmitters.containsKey(userId)) {
					SseEmitter sseEmitter = NotificationController.sseEmitters.get(userId);

					try {
						Map<String, String> eventData = new HashMap<>();
						eventData.put("message", "경매 종료 1시간전 입니다.");
						eventData.put("goodsId", goods.getGoodsId().toString()); //경매 상품 id
						eventData.put("goodsName", goods.getGoodsName()); //경매 상품 이름

						sseEmitter.send(SseEmitter.event().name("DeadLine").data(eventData));

						// DB 저장
						NotificationEntity notification = NotificationEntity.builder()
								.notificationType(NotificationType.DEADLINE)
								.content(goods.getGoodsName())
								.goods(goods)
								.sender(goods.getUser())
								.build();
						notificationRepository.save(notification);
					} catch (Exception e) {
						NotificationController.sseEmitters.remove(userId);
					}
				}
			}
		}
	}

	public List<NotificationListResDto> getNotifications(CustomOauth2User user) {
		UserEntity findUser = userRepository.findById(user.getUserId())
				.orElseThrow(() -> new NullPointerException("존재하지 않는 회원입니다."));
		List<NotificationEntity> findNotificationList = notificationRepository.findBySenderAndNotificationTypeNot(
				findUser, NotificationType.CHAT);

		return findNotificationList.stream()
				.map(notification -> NotificationListResDto.builder()
						.id(notification.getId())
						.notificationType(notification.getNotificationType())
						.goodsName(notification.getContent())
						.createdAt(notification.getCreatedAt())
						.goodsId(notification.getGoods().getGoodsId())
						.sender(notification.getSender().getNickName())
						.price(notification.getGoods().getCurrentBidPrice())
						.endTime(Duration.between(LocalDateTime.now(), notification.getGoods().getActionEndTime()))
						.build()).toList();
	}
}
