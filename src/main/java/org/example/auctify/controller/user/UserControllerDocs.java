package org.example.auctify.controller.user;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Pattern;
import org.example.auctify.dto.Goods.GoodsResponseSummaryDTO;
import org.example.auctify.dto.Goods.ReviewDetailResponseDTO;
import org.example.auctify.dto.Goods.GoodsResponseDTO;
import org.example.auctify.dto.duplication.DuplicationDTO;
import org.example.auctify.dto.like.LikeGoodsResponseDTO;
import org.example.auctify.dto.response.ApiResponseDTO;
import org.example.auctify.dto.social.CustomOauth2User;
import org.example.auctify.dto.user.*;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "유저", description = "유저 API")
public interface UserControllerDocs {


    @Operation(summary = "마이페이지 개인 정보", description = "자신의 프로필 정보를 전달")
    ResponseEntity<ApiResponseDTO<UserInfoResponseDTO>> getMyProfile(@AuthenticationPrincipal CustomOauth2User userDetails);

    @Operation(summary = "프로필 개인 정보", description = "검색 유저의 프로필 정보를 전달")
    ResponseEntity<ApiResponseDTO<UserInfoResponseDTO>> getProfile(long userId);

    @Operation(summary = "프로필 변경", description = "프로필 사진과 닉네임을 변경하는 API " +
            "주소는 이미 저장된 주소중 기본 주소를 정하는 거다.")
    ResponseEntity<ApiResponseDTO<UserChangedProfileResponseDTO>> changeProfile(@Validated UserInfoRequestDTO profile, BindingResult bindingResult, @AuthenticationPrincipal CustomOauth2User userDetails);


    @Operation(summary = "토탈 거래 후기 확인", description = "입찰 혹은 낙찰로 받은 거래후기들을 확인 할 수 있는 API" +
            "(시간순으로 입찰과 낙찰 받은 후기를 함께 내림차순으로 전달해준다.)")
    ResponseEntity<ApiResponseDTO<Page<ReviewDetailResponseDTO>>> getListTotalFeedback(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal CustomOauth2User userDetails);


    // 다시 이야기가 필요할 듯
//    @Operation(summary = "받은 매너 리스트 및 평균 매너", description = "받은 매너 온도를 확인할 수 있는 시스템")
//    ResponseEntity<ApiResponseDTO<Page<MannerTemperatureDTO>>> getMannerTemperature(
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size,
//            @AuthenticationPrincipal CustomOauth2User userDetails);


    @Operation(summary = "내가 등록한 경매", description = "내가 등록한 경매 정보를 반환하는 API")
    ResponseEntity<ApiResponseDTO<Page<GoodsResponseSummaryDTO>>> getMyGoods(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal CustomOauth2User userDetails
    );

    @Operation(summary = "내가 입찰한 경매, 낙찰 경매품", description = "내가 입찰하거나 낙찰 받은 경매품리스트 API")
    ResponseEntity<?> getMyAuctifyGoods(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal CustomOauth2User userDetails
    );


    @Operation(summary = "좋아요 누른 경매품", description = "좋아요 누른 경매리스트를 가져오는 API")
    ResponseEntity<ApiResponseDTO<Page<LikeGoodsResponseDTO>>> getMyLikeGoods(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal CustomOauth2User userDetails
    );

    @Operation(summary = "주소 리스트를 반환", description = "주소 리스트를 전달하는 API " +
            "(대표 주소는 반드시 선택되게 만듦)")
    ResponseEntity<ApiResponseDTO<List<AddressDTO>>>  getListAddress(
            @AuthenticationPrincipal CustomOauth2User userDetails);


    @Operation(summary = "주소 등록", description = "주소 등록하고 등록된 주소를 전달 ")
    ResponseEntity<ApiResponseDTO<AddressDTO>> postAddress(
            @Validated AddressRequestDTO addressRequestDTO,
            BindingResult bindingResult,
            CustomOauth2User userDetails);





        @Operation(summary = "이메일 중복 확인", description = "이메일 중복 확인하는 API (참고 : 최초 가입시에는 중복될 수 있음 소셜 로그인에서 받아오기 때문)")
        public ResponseEntity<ApiResponseDTO<DuplicationDTO>> checkEmail(
                @RequestParam
                String email);


        @Operation(summary = "닉네임 중복을 확인하는 API", description = "닉네임 중복 여부를 확인하는 API (참고 : 최초 가입시에는 중복될 수 있음 소셜 로그인에서 받아오기 때문)")
        public ResponseEntity<ApiResponseDTO<DuplicationDTO>> checkNickname(
                @RequestParam
                String nickname) ;

    @Operation(summary = "기본 주소를 정하는 API", description = "기본 주소를 변경하는 API 등록된 주소중에서 원하는 주소를 전달하면 해당 주소로 기본 주소가 설정된다. \n 기존에 다른 기본 주소는 기본주소가 아니게 된다. \n 주소 번호 전달 필수 ")
    public ResponseEntity<ApiResponseDTO<AddressDTO>> changeDefaultAddress(
            AddressDTO defaultAddressDTO
            ,CustomOauth2User userDetails);


    //Last-Event-ID는 SSE 연결이 끊어질 경우,
    // 클라이언트가 수신한 마지막 데이터의 id값을 의미합니다. 항상 존재하는 것이 아니기 때문에 false
    @Operation(summary = "알람을 구독함 ", description = "알람을 구독하는 API" +
            "(SSE연결을 위한 API이다 !!")
    ResponseEntity<?> subscribe(@AuthenticationPrincipal CustomOauth2User userDetails, @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId);

}
