package org.example.auctify.controller.auction;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.auctify.dto.Goods.*;
import org.example.auctify.dto.response.ApiResponseDTO;
import org.example.auctify.dto.social.CustomOauth2User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "경매", description = "경매 API")
public interface GoodsControllerDocs {

    // 경매 물품 정보를 반환 (상세 조회)
    @Operation(summary = "물품 정보 반환", description = "경매 물품 정보 페이지 입니다.")
    ResponseEntity<ApiResponseDTO<GoodsResponseDTO>>getGoods(
            @Parameter(description = "조회할 물품 ID", example = "1")
            @PathVariable("goodsId") Long goodsId);

    // 경매품을 등록
    @Operation(summary = "물품 등록", description = "사용자가 경매 품목을 등록합니다.")
    ResponseEntity<ApiResponseDTO<GoodsRequestDTO>> createGoods(
            @RequestBody GoodsRequestDTO goodsRequestDTO
    );

    // 실시간 입찰
    @Operation(summary = "실시간 입찰", description = "사용자가 경매 품목을 입찰합니다.")
    ResponseEntity<ApiResponseDTO<GoodsRequestDTO>> createBid(
            @RequestBody BidRequestDTO goodsRequestDTO,
            @AuthenticationPrincipal CustomOauth2User userDetails
    );

    // 실시간 입찰 취소
    @Operation(summary = "실시간 입찰 취소", description = "사용자가 경매 품목을 입찰 취소합니다.")
    ResponseEntity<ApiResponseDTO<GoodsRequestDTO>> cancelBid(
            @RequestBody BidRequestDTO goodsRequestDTO,
            @AuthenticationPrincipal CustomOauth2User userDetails
    );

    // 사용자는 경매 리스트 조회 가능
    @Operation(summary = "물품 검색 결과창", description = "사용자가 물품을 검색하고, 페이지네이션으로 반환합니다.")
    ResponseEntity<ApiResponseDTO<GoodsResponseDTO>> searchGoods(
            @RequestParam(required = false) String category, // 카테고리
            @RequestParam(required = false) double priceRangeLow, // 현재 입찰가 범위 low
            @RequestParam(required = false) double priceRangeHigh, // 현재 입찰가 high
            @RequestParam(required = false) boolean isNew, // 새상품 | 중고 여부
            @RequestParam(required = false) String auctionStatus, // 경매 상태
            @RequestParam(required = false) String auctionTitle,  // 경매 제목
            @RequestParam(required = false) String sort,  // 경매 제목
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    );

    // 매너 온도 평가 가능 + 후기
    @Operation(summary = "경매 등록자 및 낙찰자가 매너온도및 후기를 작성할 수 있음.", description = "경매 등록자 및 낙찰자가 매너온도및 후기를 작성할 수 있습니다.")
    ResponseEntity<ApiResponseDTO<List<ReviewDetailResponseDTO>>> createReview(
            ReviewRequestDTO reviewRequestDTO,
            @AuthenticationPrincipal CustomOauth2User userDetails
    );

    // 좋아요(찜)을 하거나 취소 가능
    @Operation(summary = "모든 유저는 상품에 좋아요(찜)를 누를 수 있다.", description = "경매품에 좋아요(찜)를 누를 수 있고 해당 상품에 좋아요(찜)를 눌렀으면 취소도 가능한 API")
    ResponseEntity<ApiResponseDTO<LikeResponseDTO>> changeLike(
            LikeRequestDTO likeRequestDTO,
            @AuthenticationPrincipal CustomOauth2User userDetails
    );

    // 낙찰자가 실제 결제 후 정보를 등록하는 API
    @Operation(summary = "낙찰 후 낙찰구매 정보를 등록할 수 있다.", description = "낙찰 후 결제하면서 정보를 입력하면 정보를 등록할 수 있는 API")
    ResponseEntity<ApiResponseDTO<BidPurchaseResponseDTO>> create(
            BidPurchaseRequestDTO bidPurchaseRequestDTO,
            @AuthenticationPrincipal CustomOauth2User userDetails
    );
}
