package org.example.auctify.dto.bid;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BidHistoryResponseDTO {

    @Schema(description = "입찰id")
    private Long bidHistoryId;

    @Schema(description = "상품id")
    private Long goodsId;

    @Schema(description = " 상품(경매) 이름")
    private String goodsName;

    @Schema(description = "상품에 대한 입찰 취소 여부")
    private boolean cancelFlag;

    @Schema(description = "상품 낙찰여부")
    private boolean goodsProcessStatus;

    @Schema(description = "등록된 첫번째 이미지")
    private String imageUrls;

    @Schema(description = "현재 나의 입찰가")
    private Long bidPrice;

    @Schema(description = "현재 상품의 최고 입찰가")
    private Long bidMaxPrice;




}
