package org.example.auctify.controller.chat;


import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.auctify.dto.chat.CreateOneToOneChatReqDto;
import org.example.auctify.dto.chat.GetMessageHistoryResDto;
import org.example.auctify.dto.chat.GetOneToOneChatRoomListResDto;
import org.example.auctify.dto.response.ApiResponseDTO;
import org.example.auctify.dto.social.CustomOauth2User;
import org.example.auctify.service.chat.ChatRoomService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chatRoom")
public class ChatRoomController implements ChatRoomControllerDocs{
	private final ChatRoomService chatRoomService;

	//채팅방 생성
	@PostMapping
	public ResponseEntity<ApiResponseDTO<Long>> createOneToOneChatRoom(
			@RequestBody CreateOneToOneChatReqDto createOneToOneChatReqDto,
			@AuthenticationPrincipal CustomOauth2User user
	) {
		if (createOneToOneChatReqDto.getTheOtherUserId() == user.getUserId()) {
			throw new IllegalArgumentException("혼자서 채팅방을 생성할 수 없습니다.");
		}

		Long chatRoomId = chatRoomService.createOneToOneChatRoom(createOneToOneChatReqDto, user);

		if (chatRoomId == -1L) {
			throw new IllegalArgumentException("이미 대화했던 상품입니다.");
		}
		return ResponseEntity.ok(ApiResponseDTO.success(chatRoomId));
	}

	//채팅방 나가기
	@PatchMapping("/{chatroomId}")
	public ResponseEntity<Void> exitChatRoom(@PathVariable("chatroomId") Long chatRoomId, @AuthenticationPrincipal CustomOauth2User user) {
		chatRoomService.exitChatRoom(chatRoomId, user);

		return new ResponseEntity<>(HttpStatus.OK);
	}

	//채팅방 목록 조회
	@GetMapping
	public ResponseEntity<ApiResponseDTO<List<GetOneToOneChatRoomListResDto>>> getOneToOneChatRoomList(
			@AuthenticationPrincipal CustomOauth2User user) {
		List<GetOneToOneChatRoomListResDto> chatRoomList = chatRoomService.getOneToOneChatRoomList(user);

		return ResponseEntity.ok(ApiResponseDTO.success(chatRoomList));
	}


	//메시지 내역 조회
	@GetMapping("/{chatroomId}}")
	public ResponseEntity<ApiResponseDTO<List<GetMessageHistoryResDto>>> getMessageHistory(
			@AuthenticationPrincipal CustomOauth2User user,
			@PathVariable("chatroomId") Long chatRoomId) {
		List<GetMessageHistoryResDto> messageHistory = chatRoomService.getMessageHistory(user, chatRoomId);
		return ResponseEntity.ok(ApiResponseDTO.success(messageHistory));
	}

	// 채팅방에 연결되어있는지 확인하는 API
	@GetMapping("/connection/{chatRoomId}")
	public ResponseEntity<ApiResponseDTO<String>> checkAlreadyConnected(@AuthenticationPrincipal CustomOauth2User user, @PathVariable Long chatRoomId) {
		if (chatRoomService.isAlreadyConnected(chatRoomId, user)) {
			return ResponseEntity.ok(ApiResponseDTO.success("채팅방에 중복접속이 불가능합니다."));
		} else {
			return  ResponseEntity.ok(ApiResponseDTO.success("채팅방에 접속중인 유저가 아닙니다."));
		}
	}

	// 대화가 가능한 채팅방인지 확인하는 API
	@GetMapping("/check/{chatRoomId}")
	public ResponseEntity<ApiResponseDTO<String>> isPossibleChatRoom(@AuthenticationPrincipal CustomOauth2User user, @PathVariable Long chatRoomId) {
		Boolean isPossible = chatRoomService.isPossibleChatRoom(user, chatRoomId);

		if (isPossible) {
			return ResponseEntity.ok(ApiResponseDTO.success("대화가 가능한 채팅방입니다."));
		} else {
			return ResponseEntity.ok(ApiResponseDTO.success("상대방이 나간 채팅방입니다."));
		}
	}

}
