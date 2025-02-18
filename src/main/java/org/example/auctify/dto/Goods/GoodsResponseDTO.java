package org.example.auctify.dto.Goods;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GoodsResponseDTO {

    @Schema(description = "상품id입니다.")
    private Long goodsId;

    @Schema(description = "상품id입니다.")
    private String goodsName;

    @Schema(description = "상품 설명입니다.")
    private String goodsDescription;

    @Schema(description = "즉시 구매가 입니다.")
    private Long buyNowPrice;
    @Schema(description = "경매 진행 상태입니다.")
    private String goodsProcessStatus;
    @Schema(description = "상품 상태입니다.")
    private String goodsStatus;
    @Schema(description = "최소 경매 진행가 입니다.")
    private Long minimumBidAmount;
    @Schema(description = "경매 종료 시각입니다.")
    private LocalDateTime actionEndTime;
    @Schema(description = "경매 주인 user id 입니다.")
    private Long userId;
    @Schema(description = "상품 카테고리 id 입니다.")
    private Long categoryId;

}
