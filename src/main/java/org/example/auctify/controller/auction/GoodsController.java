package org.example.auctify.controller.auction;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.example.auctify.dto.Goods.*;
import org.example.auctify.dto.bid.BidHistoryResponseDTO;
import org.example.auctify.dto.bid.BidSummaryDTO;
import org.example.auctify.dto.response.ApiResponseDTO;
import org.example.auctify.dto.social.CustomOauth2User;
import org.example.auctify.service.auction.GoodsService;
import org.example.auctify.service.notification.NotificationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * worker : 조영흔
 * work : 요구사항을 보고 수정 작업 진행
 * date    : 2025/03/12
 */

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auction") //검색,물품정보,물품 등록
@Log4j2
@Validated
public class GoodsController implements GoodsControllerDocs {


    private final GoodsService goodsService;
    private final NotificationService notificationService;



    // 경매 물품 정보를 반환 (상세 조회)

    @GetMapping("/{goodsId}")
    public ResponseEntity<ApiResponseDTO<GoodsResponseDTO>> getGoods(
            CustomOauth2User userDetails,
        @Parameter(description = "조회할 물품 ID", example = "1")
        @PathVariable("goodsId") Long goodsId) {
        try {
            // 로그인 여부에 따라 userId를 넘길지 결정
            Long userId = (userDetails != null) ? userDetails.getUserId() : null;

            GoodsResponseDTO goodsResponseDTO = goodsService.searchGoodsId(userId,goodsId);
            return ResponseEntity.ok(ApiResponseDTO.success(goodsResponseDTO));
        } catch (Exception e) {
            log.error("[LOG] Internal server error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponseDTO.error(400, "Get Auction Goods"));
        }
    }

    //경매품을 등록
    @PostMapping
    public ResponseEntity<ApiResponseDTO<GoodsResponseDTO>> createGoods(
            CustomOauth2User userDetails,
            @RequestBody GoodsRequestDTO goodsRequestDTO
    ) {
        try {
            Long userId = userDetails.getUserId();
            GoodsResponseDTO goodsResponseDTO = goodsService.createActionsGoods(userId, goodsRequestDTO);
            return ResponseEntity.ok(ApiResponseDTO.success(goodsResponseDTO));
        } catch (Exception e) {
            log.error("[LOG] Internal server error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponseDTO.error(400, "Create Auction Goods"));
        }
    }

    // 실시간 입찰
    @PostMapping("/bid")
    public ResponseEntity<ApiResponseDTO<BidHistoryResponseDTO>> createBid(
            @RequestBody BidRequestDTO bidRequestDTO,
            CustomOauth2User userDetails
    ) {
        try {
            Long userId = userDetails.getUserId();
            BidHistoryResponseDTO bidHistoryResponseDTO = goodsService.bidAuctionGoods(userId, bidRequestDTO);

            //입찰 알림
            notificationService.notifyBid(bidHistoryResponseDTO.getGoodsId());
            notificationService.sendBidUpdate(bidHistoryResponseDTO.getGoodsId());

            return ResponseEntity.ok(ApiResponseDTO.success(bidHistoryResponseDTO));
        } catch (Exception e) {
            log.error("[LOG] Internal server error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponseDTO.error(400, "Bid Auctions Goods"));
        }
    }

    // 실시간 입찰 취소
    @PutMapping("/bid/{bidHistoryId}")
    public ResponseEntity<ApiResponseDTO<BidHistoryResponseDTO>> cancelBid(
            Long bidHistoryId,
            @AuthenticationPrincipal CustomOauth2User userDetails
    ) {
        try {
            Long userId = userDetails.getUserId();
            BidHistoryResponseDTO bidHistoryResponseDTO = goodsService.cancelBidAuctions(userId, bidHistoryId);
            return ResponseEntity.ok(ApiResponseDTO.success(bidHistoryResponseDTO));
        } catch (Exception e) {
            log.error("[LOG] Internal server error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponseDTO.error(400, "Bid Cancel Auctions Goods"));
        }
    }


    //사용자는 경매 리스트 조회 가능
    //Pageable 로 반환, 페이지 사이즈는 요청에 따라 변경 가능
    @GetMapping("/search")
    public ResponseEntity<ApiResponseDTO<Page<GoodsResponseSummaryDTO>>> searchGoods(
            CustomOauth2User userDetails,
            String category,
            Double priceRangeLow,
            Double priceRangeHigh,
            String goodsStatus,
            String goodsProcessStatus,
            String goodsName,
            String sort,
            int page,
            int size
    ) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(sort).descending());
            // 로그인 여부에 따라 userId를 넘길지 결정
            Long userId = (userDetails != null) ? userDetails.getUserId() : -1;

            Page<GoodsResponseSummaryDTO> result =
                    goodsService.searchGoods(userId, category, priceRangeLow, priceRangeHigh, goodsStatus,goodsProcessStatus,goodsName,sort,pageable);
            return ResponseEntity.ok(ApiResponseDTO.success(result));
        } catch (Exception e) {
            log.error("[LOG] Internal server error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseDTO.error(500, "Search Goods Error"));
        }
    }

    // 경매 디테일에서 최근 경매 내역
    @GetMapping("/bidSummary")
    public ResponseEntity<ApiResponseDTO<List<BidSummaryDTO>>> getGoodsBidList(Long goodsId,Long size) {
        try {
            goodsService.getBidHistorySummary(goodsId, size);
            return ResponseEntity.ok(ApiResponseDTO.success(goodsService.getBidHistorySummary(goodsId, size)));
        } catch (Exception e) {
            log.error("[LOG] Internal server error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseDTO.error(500, "Search Goods Error"));
        }
    }

    //매너 온도 평가 가능 + 후기 구매자만 후기를 작성할 수 있다.
    @PostMapping("/review/{goodsId}")
    public ResponseEntity<ApiResponseDTO<ReviewDetailResponseDTO>> createReview(
            ReviewRequestDTO reviewRequestDTO,
            @AuthenticationPrincipal CustomOauth2User userDetails
    ) {
        try {
            Long userId = userDetails.getUserId();
            ReviewDetailResponseDTO reviewDetailResponseDTO = goodsService.createReview(userId, reviewRequestDTO);

            return ResponseEntity.ok(ApiResponseDTO.success(reviewDetailResponseDTO));
        } catch (Exception e) {
            log.error("[LOG] Internal server error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponseDTO.error(400, "create Review"));
        }
    }


    @PostMapping("/liked/{goodsId}")
    public ResponseEntity<ApiResponseDTO<String>> createLike(
            Long goodsId,
            LikeRequestDTO likeRequestDTO,
            @AuthenticationPrincipal CustomOauth2User userDetails
    ) {
        try {
            Long userId = userDetails.getUserId();
            goodsService.createLike(userId, goodsId);
            return ResponseEntity.ok(ApiResponseDTO.success("좋아요로 바뀜"));
        } catch (Exception e) {
            log.error("[LOG] Internal server error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponseDTO.error(400, "Goods Like"));
        }
    }

    @DeleteMapping("/liked/{goodsId}")
    public ResponseEntity<ApiResponseDTO<String>> cancelLike(
            Long goodsId,
            LikeRequestDTO likeRequestDTO,
            @AuthenticationPrincipal CustomOauth2User userDetails
    ) {
        try {
            Long userId = userDetails.getUserId();
            goodsService.cancelLike(userId, goodsId);
            return ResponseEntity.ok(ApiResponseDTO.success("좋아요 상태 취소 "));
        } catch (Exception e) {
            log.error("[LOG] Internal server error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponseDTO.error(400, "Goods Like"));
        }
    }

    // 낙찰자가 실제 결제후 정보를 등록하는 API
//    @Operation(summary = "낙찰 후 낙찰구매 정보를 등록할 수 있다.", description = "낙찰 후 결제하면서 정보를 입력하면 정보를 등록할 수 있는  API")
//    @PostMapping("/{goodsId}/{bidId}")
//    public ResponseEntity<ApiResponseDTO<BidPurchaseResponseDTO>> create(
//            BidPurchaseRequestDTO bidPurchaseRequestDTO,
//            @AuthenticationPrincipal CustomOauth2User userDetails
//    ) {
//        return ResponseEntity.ok(null);
//    }


}
