package org.example.auctify.service.chat;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.example.auctify.dto.chat.CreateOneToOneChatReqDto;
import org.example.auctify.dto.chat.GetMessageHistoryResDto;
import org.example.auctify.dto.chat.GetOneToOneChatRoomListResDto;
import org.example.auctify.dto.chat.UserChatRoomStatus;
import org.example.auctify.dto.social.CustomOauth2User;
import org.example.auctify.entity.Goods.GoodsEntity;
import org.example.auctify.entity.chat.ChatMessageEntity;
import org.example.auctify.entity.chat.ChatRoomEntity;
import org.example.auctify.entity.chat.UserChatRoomEntity;
import org.example.auctify.entity.user.UserEntity;
import org.example.auctify.repository.chat.ChatMessageRepository;
import org.example.auctify.repository.chat.ChatRoomRepository;
import org.example.auctify.repository.chat.UserChatRoomRepository;
import org.example.auctify.repository.goods.GoodsRepository;
import org.example.auctify.repository.user.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
	private final UserRepository userRepository;
	private final ChatRoomRepository chatRoomRepository;
	private final UserChatRoomRepository userChatRoomRepository;
	private final GoodsRepository goodsRepository;
	private final ChatMessageRepository chatMessageRepository;

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

			return chatRoom.getId();
		}
	}

	@Transactional
	public void exitChatRoom(Long chatRoomId, CustomOauth2User user) {
		UserEntity findUser = userRepository.findById(user.getUserId())
				.orElseThrow(() -> new NullPointerException("존재하지 않는 회원입니다."));

		ChatRoomEntity findChatRoom = chatRoomRepository.findById(chatRoomId)
				.orElseThrow(() -> new NullPointerException("존재 하지 않는 채팅방 입니다."));

		List<UserChatRoomEntity> findUserChatRoom = userChatRoomRepository.findAllByUserAndChatRoom(
				findUser, findChatRoom);

		findUserChatRoom.get(0).inActive();
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
			userIds.remove(user.getUserId()); // 현재 사용자의 ID를 제거하면 상대방의 ID만 남는다
			Long theOtherUserId = userIds.get(0);

			UserEntity findTheOtherUser = userRepository.findById(theOtherUserId)
					.orElseThrow(() -> new NullPointerException("존재 하지 않는 유저 입니다."));

			Page<ChatMessageEntity> findMessage = chatMessageRepository.findByChatRoomIdOrderBySendDateDesc(
					findChatRoom.getId(), PageRequest.of(0, 1));

			ChatMessageEntity lastMessage = findMessage.hasContent() ? findMessage.getContent().get(0) : null;

			return GetOneToOneChatRoomListResDto.builder()
					.roomTitle(findChatRoom.getGoods().getGoodsName()) /*findTheOtherUser.getNickName())*/
					.chatRoomId(findChatRoom.getId())
					.goodsId(findChatRoom.getGoods().getGoodsId())
					.theOtherUserId(theOtherUserId)
					.theOtherUserImage(findTheOtherUser.getImage())
					.lastMessage(lastMessage == null ? "" : lastMessage.getContent())
					.lastMessageTime(lastMessage == null ? null : lastMessage.getSendDate())
					.build();
		}).collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public GetMessageHistoryResDto getMessageHistory(CustomOauth2User user, Long chatRoomId, Long goodsId) {
		List<ChatMessageEntity> findMessages = chatMessageRepository.findByChatRoomIdAndUserIdNotOrderBySendDateAsc(
				chatRoomId, user.getUserId());

		// 메시지 내용과 보낸 시간을 리스트로 변환합니다.
		List<String> contents = findMessages.stream()
				.map(ChatMessageEntity::getContent)
				.toList();

		List<LocalDateTime> sendTimes = findMessages.stream()
				.map(ChatMessageEntity::getSendDate)
				.toList();

		GoodsEntity findGoods = goodsRepository.findById(goodsId)
				.orElseThrow(() -> new NullPointerException("상품을 찾을 수 없습니다."));

		return GetMessageHistoryResDto.builder()
				.contents(contents)
				.sendTimes(sendTimes)
				.goodsName(findGoods.getGoodsName())
				.build();
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
}
