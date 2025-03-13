package org.example.auctify.controller.auction;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.example.auctify.dto.Goods.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * worker : 조영흔
 * work : 요구사항을 보고 수정 작업 진행
 * date    : 2025/03/12
 */


@RestController
@RequestMapping("/api/auction") //검색,물품정보,물품 등록
public class Goodscontroller implements GoodsControllerDocs{


    // 경매 물품 정보를 반환 (상세 조회)
    @Operation(summary = "물품 정보 반환", description = "경매 물품 정보 페이지 입니다.")
    @GetMapping("/{goodsId}")
    public ResponseEntity<GoodsResponseDTO> getGoods(
            @Parameter(description = "조회할 물품 ID", example = "1")
            @PathVariable("goodsId") Long goodsId){
        return ResponseEntity.ok(null);
        //TODO
    }


    //경매품을 등록
    @Operation(summary = "물품 등록", description = "사용자가 경매 품목을 등록합니다.")
    @PostMapping
    public ResponseEntity<GoodsRequestDTO> createGoods(
            @RequestBody GoodsRequestDTO goodsRequestDTO
    ){
        return ResponseEntity.ok(null);
        //TODO
    }

    // 실시간 입찰
    @Operation(summary = "실시간 입찰", description = "사용자가 경매 품목을 입찰합니다.")
    @PostMapping("/bid")
    public ResponseEntity<GoodsRequestDTO> createBid(
            @RequestBody BidRequestDTO goodsRequestDTO,
            @AuthenticationPrincipal UserDetails userDetails
    ){
        return ResponseEntity.ok(null);
        //TODO
    }

    // 실시간 입찰 취소
    @Operation(summary = "실시간 입찰 취소", description = "사용자가 경매 품목을 입찰 취소합니다.")
    @PutMapping("/bid")
    public ResponseEntity<GoodsRequestDTO> cancelBid(
            @RequestBody BidRequestDTO goodsRequestDTO,
            @AuthenticationPrincipal UserDetails userDetails
    ){
        return ResponseEntity.ok(null);
        //TODO
    }


    //사용자는 경매 리스트 조회 가능
    //Pageable 로 반환, 페이지 사이즈는 요청에 따라 변경 가능
    @Operation(summary = "물품 검색 결과창", description = "사용자가 물품을 검색하고, 페이지네이션으로 반환합니다.")
    @GetMapping("/search")
    public ResponseEntity<GoodsResponseDTO> searchGoods(
            String category, // 카테고리
            double priceRangeLow, // 현재 입찰가 범위 low
            double priceRangeHigh, // 현재 입찰가 high
            boolean isNew, // 새상품 | 중고 여부
            String auctionStatus, // 경매 상태
            String auctionTitle,  // 경매 제목
            String sort,  // 경매 제목
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        //TODO
        return ResponseEntity.ok(null);
    }

    //매너 온도 평가 가능 + 후기 구매자만 후기를 작성할 수 있다.
    @Operation(summary = "경매 등록자 낙찰자가 매너온도및 후기 지정할 수 있음.", description = "경매 등록자 및 낙찰자가 매너온도및 후기를 작성할 " +
            "수 있다. 모든 구매를 마치고 후기를 등록하는 API")
    @PostMapping("/review/{goodsId}")
    public ResponseEntity<List<ReviewResponseDTO>> createReview(
            ReviewRequestDTO reviewRequestDTO,
            @AuthenticationPrincipal UserDetails userDetails
    ){
        return ResponseEntity.ok(null);
        //likeService.likeGoods(goodsId);
    }


    // 좋아요(찜)을 하거나 취소 가능
    @Operation(summary = "모든 유저는 상품에 좋아요(찜)를 누를 수 있다.", description = "경매품에 좋아요(찜)를 누를 수 있고 " +
            "해당 상품에 좋아요(찜)를 눌렀으면 취소도 가능한 API")
    @PutMapping("/liked/{goodsId}")
    public ResponseEntity<LikeResponseDTO> changeLike(
            LikeRequestDTO likeRequestDTO,
            @AuthenticationPrincipal UserDetails userDetails
    ){
        return null;
    }

    // 낙찰자가 실제 결제후 정보를 등록하는 API
    @Operation(summary = "낙찰 후 낙찰구매 정보를 등록할 수 있다.", description = "낙찰 후 결제하면서 정보를 입력하면 정보를 등록할 수 있는  API")
    @PostMapping("/{goodsId}/{bidId}")
    public ResponseEntity<BidPurchaseResponseDTO> create(
            BidPurchaseRequestDTO bidPurchaseRequestDTO,
            @AuthenticationPrincipal UserDetails userDetails
    ){
        return ResponseEntity.ok(null);
        //likeService.likeGoods(goodsId);
    }

    // swagger을 연결하는 API 실제 구현은 따로해야함
    @Operation(summary = "WebSocket 연결", description = "클라이언트와 서버 간 실시간 연결을 위한 WebSocket을 설정합니다." +
            "실제로는 따로 구현해야함 추후에 삭제 예정 (swagger를 위해서 작성함)")
    @GetMapping("/ws/connect")
    public String connectWebSocket() {
        // WebSocket 연결에 대한 로직은 Spring WebSocket 또는 다른 기술로 처리합니다.
        return "WebSocket 연결 성공! 클라이언트와 서버가 실시간 통신을 시작합니다.";
    }


}
