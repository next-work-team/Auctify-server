package org.example.auctify.dto.like;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.example.auctify.dto.Goods.GoodsCategory;
import org.example.auctify.dto.Goods.GoodsProcessStatus;
import org.example.auctify.dto.Goods.GoodsStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LikeGoodsResponseDTO {


    @Schema(description = "좋아요 Id")
    private Long likeId;

    @Schema(description = "상품id")
    private Long goodsId;

    @Schema(description = " 상품(경매) 이름")
    private String goodsName;

    @Schema(description = "등록된 첫번째 이미지")
    private String imageUrls;

    @Schema(description = "경매 종료 시간")
    private LocalDateTime endTime;

    @Schema(description = "경매 낙찰여부")
    private GoodsProcessStatus goodsProcessStatus;

    @Schema(description = "카테고리")
    private GoodsCategory category;

    @Schema(description = "상품 상태 새것, 중고")
    private GoodsStatus goodsStatus;

    @Schema(description = "현재 입찰 수")
    private Long currentBidCount;



}
