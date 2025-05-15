package org.example.auctify.service.chat;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.auctify.dto.chat.AuctionDto;
import org.example.auctify.dto.chat.CreateOneToOneChatReqDto;
import org.example.auctify.dto.chat.GetMessageHistoryResDto;
import org.example.auctify.dto.chat.GetOneToOneChatRoomListResDto;
import org.example.auctify.dto.chat.LastMessageDto;
import org.example.auctify.dto.chat.UserChatRoomStatus;
import org.example.auctify.dto.chat.UserDto;
import org.example.auctify.dto.social.CustomOauth2User;
import org.example.auctify.entity.Goods.GoodsEntity;
import org.example.auctify.entity.chat.ChatMessageEntity;
import org.example.auctify.entity.chat.ChatRoomEntity;
import org.example.auctify.entity.chat.RedisChatRoomEntity;
import org.example.auctify.entity.chat.UserChatRoomEntity;
import org.example.auctify.entity.user.UserEntity;
import org.example.auctify.repository.chat.ChatMessageRepository;
import org.example.auctify.repository.chat.ChatRoomRepository;
import org.example.auctify.repository.chat.ConnectedChatUserRepository;
import org.example.auctify.repository.chat.CustomChatMessageRepository;
import org.example.auctify.repository.chat.UserChatRoomRepository;
import org.example.auctify.repository.goods.GoodsRepository;
import org.example.auctify.repository.user.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatRoomService {
	private final UserRepository userRepository;
	private final ChatRoomRepository chatRoomRepository;
	private final UserChatRoomRepository userChatRoomRepository;
	private final GoodsRepository goodsRepository;
	private final ChatMessageRepository chatMessageRepository;
	private final MongoTemplate mongoTemplate;
	private final ConnectedChatUserRepository connectedChatUserRepository;
	private final CustomChatMessageRepository customChatMessageRepository;

	@Transactional
	public Long createOneToOneChatRoom(CreateOneToOneChatReqDto createOneToOneChatReqDto, CustomOauth2User user) {
		UserEntity findUser = userRepository.findById(user.getUserId())
				.orElseThrow(() -> new NullPointerException("존재하지 않는 회원입니다."));

		//상대방 조회
		UserEntity findTheOtherUser = userRepository.findById(createOneToOneChatReqDto.getTheOtherUserId())
				.orElseThrow(() -> new NullPointerException("존재하지 않는 사용자입니다."));

		//roomHashCode 생성
		int roomHashCode = createRoomHashCode(findUser, findTheOtherUser, createOneToOneChatReqDto.getGoodsId());

		//채팅방 조회
		ChatRoomEntity findChatRoom = chatRoomRepository.findByRoomHashCode(roomHashCode).orElse(null);

		if (findChatRoom != null) { //기존에 대화했던적이 있는 경우
			UserChatRoomEntity findUserChatRoom = userChatRoomRepository.findByChatRoomAndUser(findChatRoom,
							findUser)
					.orElseThrow(() -> new NullPointerException("채팅방을 찾을 수 없습니다."));

			if (findUserChatRoom.getUserChatRoomStatus() == UserChatRoomStatus.INACTIVE) {// 대화했다가 나간 방인 경우
				log.info("대화했다가 나간적이 있는 방입니다. roomHashCode : {}", roomHashCode);
				return -1L;
			} else {// 대화한적이 있고 나가지 않은 방인 경우
				return findChatRoom.getId();
			}
		} else { //기존에 대화했던적이 없는 경우
			//상품 조회
			GoodsEntity findGoods = goodsRepository.findById(createOneToOneChatReqDto.getGoodsId())
					.orElseThrow(() -> new NullPointerException("해당 상품이 존재하지 않습니다."));

			//채팅방 생성
			ChatRoomEntity chatRoom = ChatRoomEntity.builder()
					.goods(findGoods)
					.roomHashCode(roomHashCode)
					.build();

			UserChatRoomEntity userChatRoom = UserChatRoomEntity.builder()
					.user(findUser)
					.chatRoom(chatRoom)
					.userChatRoomStatus(UserChatRoomStatus.ACTIVE)
					.lastExitedAt(LocalDateTime.now())
					.build();

			UserChatRoomEntity theOtherUserChatRoom = UserChatRoomEntity.builder()
					.user(findTheOtherUser)
					.chatRoom(chatRoom)
					.userChatRoomStatus(UserChatRoomStatus.ACTIVE)
					.lastExitedAt(LocalDateTime.now())
					.build();

			chatRoom.addChatUser(userChatRoom);
			chatRoom.addChatUser(theOtherUserChatRoom);

			chatRoomRepository.save(chatRoom);
			userChatRoomRepository.save(userChatRoom);
			userChatRoomRepository.save(theOtherUserChatRoom);

			log.info("채팅방 새롭게 생성 : {}", chatRoom.getId());

			return chatRoom.getId();
		}
	}

	@Transactional
	public void connectChatRoom(Long chatRoomId, long userId) {
		log.info("user 접속 redis에 저장: {}", userId);
		RedisChatRoomEntity redisChatRoom = RedisChatRoomEntity.builder()
				.id(UUID.randomUUID().toString())
				.chatRoomId(chatRoomId)
				.userId(userId)
				.build();

		connectedChatUserRepository.save(redisChatRoom);

		// 접속한 사용자 정보 조회
		UserEntity findUser = userRepository.findById(userId)
				.orElseThrow(() -> new NullPointerException("존재하지 않는 회원입니다."));

		updateReadCount(chatRoomId, findUser.getUserId());
	}

	@Transactional
	public void exitChatRoom(Long chatRoomId, CustomOauth2User user) {
		UserEntity findUser = userRepository.findById(user.getUserId())
				.orElseThrow(() -> new NullPointerException("존재하지 않는 회원입니다."));

		ChatRoomEntity findChatRoom = chatRoomRepository.findById(chatRoomId)
				.orElseThrow(() -> new NullPointerException("존재 하지 않는 채팅방 입니다."));

		List<UserChatRoomEntity> findUserChatRoom = userChatRoomRepository.findAllByUserAndChatRoom(
				findUser, findChatRoom);

		if (findUserChatRoom.size() != 1) {
			for (UserChatRoomEntity userChatRoom : findUserChatRoom) {
				log.info("조회된 pk값 : {}", userChatRoom.getId());
			}
		} else {
			findUserChatRoom.get(0).inActive();
		}

		updateReadCount(chatRoomId, findUserChatRoom.get(0).getId()); // 방을 나가는 순간 안읽은 메세지 읽음 처리
	}

	@Transactional(readOnly = true)
	public List<GetOneToOneChatRoomListResDto> getOneToOneChatRoomList(CustomOauth2User user) {
		UserEntity findUser = userRepository.findById(user.getUserId())
				.orElseThrow(() -> new NullPointerException("존재하지 않는 회원입니다."));

		List<UserChatRoomEntity> findChatRoomList = userChatRoomRepository.findAllByUserAndUserChatRoomStatus(findUser,
				UserChatRoomStatus.ACTIVE);

		if (findChatRoomList.isEmpty()) {
			return Collections.emptyList();
		}

		return findChatRoomList.stream().map(userChatRoom -> {
			ChatRoomEntity findChatRoom = chatRoomRepository.findById(userChatRoom.getChatRoom().getId())
					.orElseThrow(() -> new NullPointerException("채팅 방을 찾을 수 없습니다."));

			List<Long> userIds = findChatRoom.getChatRoomUsers().stream()
					.map(chatUser -> chatUser.getUser().getUserId())
					.collect(Collectors.toList());

			// 상대방의 ID를 찾기
			userIds.remove(findUser.getUserId()); // 현재 사용자의 ID를 제거하면 상대방의 ID만 남는다
			Long theOtherUserId = userIds.get(0);

			UserEntity findTheOtherUser = userRepository.findById(theOtherUserId)
					.orElseThrow(() -> new NullPointerException("존재 하지 않는 유저 입니다."));

			Page<ChatMessageEntity> findMessage = chatMessageRepository.findByChatRoomIdOrderBySendDateDesc(
					findChatRoom.getId(), PageRequest.of(0, 1));
			ChatMessageEntity lastMessage = findMessage.hasContent() ? findMessage.getContent().get(0) : null;

			boolean isRead = false;
			boolean isMine = false;
			Long unReadCount = Optional.of(countUnreadMessages(findChatRoom.getId(), findUser.getUserId())).orElse(0L);

			LastMessageDto lastMessageDto = null;

			if (findUser.getUserId().equals(lastMessage.getUserId())) {
				isMine = true;
				if (userChatRoom.getLastExitedAt().isAfter(lastMessage.getSendDate())) {
					isRead = true;
				}
			}

			UserDto userDto = UserDto.builder()
					.id(findTheOtherUser.getUserId())
					.name(findTheOtherUser.getNickName())
					.avatar(findTheOtherUser.getImage())
					.build();

			if (lastMessage != null) {
				lastMessageDto = LastMessageDto.builder()
						.text(lastMessage.getContent())
						.timestamp(lastMessage.getSendDate())
						.isRead(isRead)
						.isMine(isMine)
						.build();
			}

			AuctionDto auctionDto = AuctionDto.builder()
					.id(findChatRoom.getGoods().getGoodsId())
					.title(findChatRoom.getGoods().getGoodsName())
					.build();

			return GetOneToOneChatRoomListResDto.builder()
					.id(findChatRoom.getId())
					.user(userDto)
					.lastMessage(lastMessage == null ? null : lastMessageDto)
					.unreadCount(unReadCount)
					.auction(auctionDto)
					.build();
		}).collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public List<GetMessageHistoryResDto> getMessageHistory(CustomOauth2User user, Long chatRoomId) {
		List<ChatMessageEntity> findUnreadMessages = chatMessageRepository.findByChatRoomIdAndUserIdNotAndUnReadCountOrderBySendDateAsc(
				chatRoomId, user.getUserId(), 1);

		return findUnreadMessages.stream()
				.map(findMessage -> {
					UserEntity findUser = userRepository.findById(findMessage.getUserId())
							.orElseThrow(() -> new NullPointerException("존재하지 않는 회원입니다."));
					boolean isMine = false;
					if (user.getUserId().equals(findMessage.getUserId())) {
						isMine = true;
					}

					return GetMessageHistoryResDto.builder()
							.id(findMessage.getId())
							.text(findMessage.getContent())
							.timestamp(findMessage.getSendDate())
							.sender(findUser.getNickName())
							.isMine(isMine)
							.build();
				})
				.collect(Collectors.toList());
	}

	@Transactional
	public void updateReadCount(Long chatRoomId, Long userId) {
		customChatMessageRepository.updateUnReadCount(chatRoomId, userId);
	}

	@Transactional
	public void disconnectChatRoom(Long chatRoomId, Long userId) {
		RedisChatRoomEntity findRedisChatRoom = connectedChatUserRepository.findByChatRoomIdAndUserId(chatRoomId,
				userId).orElseThrow(() -> new NullPointerException("존재하지 않는 채팅방입니다."));

		log.info("user 접속 종료 redis에서 삭제 : {}", userId);
		connectedChatUserRepository.delete(findRedisChatRoom);
	}

	// 채팅방 접속중인 인원 확인
	public Integer checkConnectedUser(Long chatRoomId) {
		List<RedisChatRoomEntity> findRedisChatRoomList = connectedChatUserRepository.findAllByChatRoomId(chatRoomId);

		return findRedisChatRoomList.size();
	}

	// 채팅방 해시코드 생성
	private int createRoomHashCode(UserEntity user, UserEntity anotherUser, Long goodsId) {

		Long userId = user.getUserId();
		Long anotherId = anotherUser.getUserId();

		// userId와 anotherId에 따라 해시코드를 생성하되, goodsId를 추가
		if (userId > anotherId) {
			return Objects.hash(userId, anotherId, goodsId);
		} else {
			return Objects.hash(anotherId, userId, goodsId);
		}
	}

	@Transactional(readOnly = true)
	public boolean isAlreadyConnected(Long chatRoomId, CustomOauth2User user) {
		return connectedChatUserRepository.findByChatRoomIdAndUserId(chatRoomId, user.getUserId())
				.isPresent();
	}

	@Transactional(readOnly = true)
	public Boolean isPossibleChatRoom(CustomOauth2User user, Long chatRoomId) {
		List<UserChatRoomEntity> findUserChatRoomList = userChatRoomRepository.findAllUserChatRoomByChatRoomIdWithUser(
				chatRoomId);

		if (findUserChatRoomList.isEmpty()) {
			new NullPointerException("존재하지 않는 채팅방입니다.");
		}

		for (UserChatRoomEntity userChatRoom : findUserChatRoomList) {
			if (userChatRoom.getUser().getUserId() != user.getUserId()
					&& userChatRoom.getUserChatRoomStatus() == UserChatRoomStatus.INACTIVE) {
				return false;
			}
		}
		return true;
	}

	private long countUnreadMessages(Long chatRoomId, Long userId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("chatRoomId").is(chatRoomId));
		query.addCriteria(Criteria.where("readCount").is(1));
		query.addCriteria(Criteria.where("userId").ne(userId));

		return mongoTemplate.count(query, "chatMessage");
	}
}
