package org.example.auctify.controller.user;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.auctify.config.security.CustomUserDetails;
import org.example.auctify.dto.Goods.FeedbackDTO;
import org.example.auctify.dto.Goods.GoodsResponseDTO;
import org.example.auctify.dto.social.CustomOauth2User;
import org.example.auctify.dto.user.MannerTemperatureDTO;
import org.example.auctify.dto.user.UserInfoResponseDTO;
import org.example.auctify.dto.user.UserInfoRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "유저", description = "유저 API")
public interface UserControllerDocs {


    @Operation(summary = "마이페이지 개인 정보", description = "자신의 프로필 정보를 전달")
    ResponseEntity<UserInfoResponseDTO> getMyProfile(@AuthenticationPrincipal CustomOauth2User userDetails);

    @Operation(summary = "마이페이지 개인 정보", description = "검색 유저의 프로필 정보를 전달")
    ResponseEntity<UserInfoResponseDTO> getProfile(long userId);

    @Operation(summary = "프로필 변경", description = "프로필 사진과 닉네임을 변경하는 API")
    ResponseEntity<UserInfoResponseDTO> changeProfile(@Validated UserInfoRequestDTO profile, BindingResult bindingResult, @AuthenticationPrincipal CustomOauth2User userDetails);


    @Operation(summary = "토탈 거래 후기 확인", description = "받은 거래 후기와 작성한 거래후기들을 확인 할 수 있는 API")
    ResponseEntity<Page<FeedbackDTO>> getListTotalFeedback(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal CustomOauth2User userDetails);


    @Operation(summary = "받은 매너 리스트 및 평균 매너", description = "받은 매너 온도를 확인할 수 있는 시스템")
    ResponseEntity<Page<MannerTemperatureDTO>> getMannerTemperature(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal CustomOauth2User userDetails);


    @Operation(summary = "내가 등록한 경매", description = "내가 등록한 경매 정보를 반환하는 API")
    ResponseEntity<Page<GoodsResponseDTO>> getMyBid(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal CustomOauth2User userDetails
    );

    @Operation(summary = "내가 입찰한 경매, 낙찰 경매품", description = "내가 입찰하거나 낙찰 받은 경매품리스트 API")
    ResponseEntity<?> getAuctifyInfo(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal CustomOauth2User userDetails
    );


    @Operation(summary = "좋아요 누른 경매품", description = "좋아요 누른 경매리스트를 가져오는 API")
    ResponseEntity<Page<GoodsResponseDTO>> getInfiniteScrollPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal CustomOauth2User userDetails
    );

    @Operation(summary = "주소 리스트를 반환", description = "주소 리스트를 전달하는 API " +
            "(대표 주소는 반드시 선택되게 만듦)")
    ResponseEntity<Page<FeedbackDTO>> getListAddress(
            @AuthenticationPrincipal CustomOauth2User userDetails);

    //Last-Event-ID는 SSE 연결이 끊어질 경우,
    // 클라이언트가 수신한 마지막 데이터의 id값을 의미합니다. 항상 존재하는 것이 아니기 때문에 false
    @Operation(summary = "알람을 구독함 ", description = "알람을 구독하는 API" +
            "(SSE연결을 위한 API이다 !!" )
    ResponseEntity<?> subscribe(@AuthenticationPrincipal CustomOauth2User userDetails, @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "")String lastEventId);
}
