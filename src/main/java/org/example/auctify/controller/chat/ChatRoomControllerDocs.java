package org.example.auctify.controller.chat;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.example.auctify.dto.chat.CreateOneToOneChatReqDto;
import org.example.auctify.dto.chat.GetMessageHistoryResDto;
import org.example.auctify.dto.chat.GetOneToOneChatRoomListResDto;
import org.example.auctify.dto.response.ApiResponseDTO;
import org.example.auctify.dto.social.CustomOauth2User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "채팅방", description = "채팅방 API")
public interface ChatRoomControllerDocs {

	//채팅방 생성
	@Operation(summary = "채팅방 생성", description = "채팅방 생성 API 입니다.")
	ResponseEntity<ApiResponseDTO<Long>> createOneToOneChatRoom(
			@RequestBody CreateOneToOneChatReqDto createOneToOneChatReqDto,
			@AuthenticationPrincipal CustomOauth2User user
	);

	//채팅방 나가기
	@Operation(summary = "채팅방 나가기", description = "채팅방 나가기로 상태 변경해주는 API입니다.")
	ResponseEntity<Void> exitChatRoom(
			@PathVariable("chatroomId") Long chatRoomId,
			@AuthenticationPrincipal CustomOauth2User user
	);

	//채팅방 목록 조회
	@Operation(summary = "채팅방 목록 조회", description = "채팅방 목록 조회 페이지입니다.")
	ResponseEntity<ApiResponseDTO<List<GetOneToOneChatRoomListResDto>>> getOneToOneChatRoomList(
			@AuthenticationPrincipal CustomOauth2User user
	);

	//메시지 내역 조회
	@Operation(summary = "메시지 내역 조회", description = "메시지 내역 조회 페이지입니다.")
	ResponseEntity<ApiResponseDTO<List<GetMessageHistoryResDto>>> getMessageHistory(
			@AuthenticationPrincipal CustomOauth2User user,
			@PathVariable("chatroomId") Long chatRoomId
	);

	// 채팅방에 연결되어있는지 확인하는 API
	@Operation(summary = "채팅방에 연결되어있는지 확인", description = "채팅방에 연결되어있는지 확인하는 API입니다.")
	public ResponseEntity<ApiResponseDTO<String>> checkAlreadyConnected(@AuthenticationPrincipal CustomOauth2User user,
																		@PathVariable Long chatRoomId);

	// 대화가 가능한 채팅방인지 확인하는 API
	@Operation(summary = "대화가 가능한 채팅방인지 확인", description = "대화가 가능한 채팅방인지 확인하는 API입니다.")
	public ResponseEntity<ApiResponseDTO<String>> isPossibleChatRoom(@AuthenticationPrincipal CustomOauth2User user,
																	 @PathVariable Long chatRoomId);
}
