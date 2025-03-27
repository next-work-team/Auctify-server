package org.example.auctify.dto.Goods;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GoodsResponseSummaryDTO {


    @Schema(description = "상품id")
    private Long goodsId;

    @Schema(description = " 상품(경매) 이름")
    private String goodsName;

    @Schema(description = "경매 낙찰여부")
    private GoodsProcessStatus goodsProcessStatus;

    @Schema(description = "현재 최고 입찰가")
    private Long currentBidPrice;

    @Schema(description = "등록된 첫번째 이미지")
    private String imageUrls;

    @Schema(description = "경매 종료 시간")
    private LocalDateTime endTime;

    @Schema(description = "카테고리")
    private GoodsCategory category;

    @Schema(description = "상품 상태 새것, 중고")
    private GoodsStatus goodsStatus;

    @Schema(description = "현재 입찰 수")
    private Long currentBidCount;




}
