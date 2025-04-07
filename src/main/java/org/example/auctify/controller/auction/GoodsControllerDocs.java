package org.example.auctify.controller.auction;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.auctify.dto.Goods.*;
import org.example.auctify.dto.bid.BidHistoryResponseDTO;
import org.example.auctify.dto.bid.BidSummaryDTO;
import org.example.auctify.dto.response.ApiResponseDTO;
import org.example.auctify.dto.social.CustomOauth2User;
import org.springframework.data.domain.Page;
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
    ResponseEntity<ApiResponseDTO<GoodsResponseDTO>> createGoods(
            @AuthenticationPrincipal CustomOauth2User userDetails,
            @RequestBody GoodsRequestDTO goodsRequestDTO
    );

    // 실시간 입찰
    @Operation(summary = "실시간 입찰", description = "사용자가 경매 품목을 입찰합니다.")
    ResponseEntity<ApiResponseDTO<BidHistoryResponseDTO>> createBid(
            @RequestBody BidRequestDTO bidRequestDTO,
            @AuthenticationPrincipal CustomOauth2User userDetails
    );

    // 실시간 입찰 취소
    @Operation(summary = "실시간 입찰 취소", description = "사용자가 경매 품목을 입찰 취소합니다.")
    ResponseEntity<ApiResponseDTO<BidHistoryResponseDTO>>cancelBid(
            @PathVariable  Long bidRequestDTO,
            @AuthenticationPrincipal CustomOauth2User userDetails
    );

    // 사용자는 경매 리스트 조회 가능
    @Operation(summary = "물품 검색 결과창", description = "사용자가 물품을 검색하고, 페이지네이션으로 반환합니다.")
    ResponseEntity<ApiResponseDTO<Page<GoodsResponseSummaryDTO>>> searchGoods (
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Double priceRangeLow,
            @RequestParam(required = false) Double priceRangeHigh,
            @RequestParam(required = false) String goodsStatus,
            @RequestParam(required = false) String goodsProcessStatus,
            @RequestParam(required = false) String goodsName,
            @RequestParam(defaultValue = "createdAt") String sort,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    );

    @Operation(summary = "상품 최근 입찰내역 15개", description = "최근 입찰내역 size개만큼(기본 15)  반환하여 준다.")
    ResponseEntity<ApiResponseDTO<List<BidSummaryDTO>>> getGoodsBidList(@RequestParam Long goodsId, @RequestParam(defaultValue = "15")Long size);


    // 매너 온도 평가 가능 + 후기
    @Operation(summary = "경매 등록자 및 낙찰자가 매너온도및 후기를 작성할 수 있음.", description = "경매 등록자 및 낙찰자가 매너온도및 후기를 작성할 수 있습니다.")
    ResponseEntity<ApiResponseDTO<ReviewDetailResponseDTO>>  createReview(
            ReviewRequestDTO reviewRequestDTO,
            @AuthenticationPrincipal CustomOauth2User userDetails
    );

    // 좋아요(찜)을 하거나 취소 가능
    @Operation(summary = "상품에 좋아요 누르기.", description = "좋아요 누르는 API")
    ResponseEntity<ApiResponseDTO<String>> createLike(
            @PathVariable  Long bidRequestDTO,
            LikeRequestDTO likeRequestDTO,
            @AuthenticationPrincipal CustomOauth2User userDetails
    );

    // 좋아요(찜)을 하거나 취소 가능
    @Operation(summary = "상품에 좋아요 취소.", description = "좋아요 취소하기 API")
    ResponseEntity<ApiResponseDTO<String>> cancelLike(
            @PathVariable  Long bidRequestDTO,
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
