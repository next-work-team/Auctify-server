package org.example.auctify.dto.Goods;


import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GoodsResponseDTO {

    @Schema(description = "상품id입니다.")
    private Long goodsId;

    @Schema(description = " 상품(경매) 이름입니다.")
    private String goodsName;

    @Schema(description = "상품 설명입니다.")
    private String goodsDescription;

    @Schema(description = "즉시 구매가 입니다.")
    private Long buyNowPrice;
    @Schema(description = "경매 진행 상태입니다.")
    private GoodsProcessStatus goodsProcessStatus;
    @Schema(description = "상품 상태입니다.")
    private GoodsStatus goodsStatus;
    @Schema(description = "최소 경매 진행가 입니다.")
    private Long minimumBidAmount;
    @Schema(description = "경매 종료 시각입니다.")
    private LocalDateTime actionEndTime;
    @Schema(description = "경매 주인 user id 입니다.")
    private Long userId;
    @Schema(description = "상품 카테고리 id 입니다.")
    private GoodsCategory category;

    @Schema(description = "현재입찰가 입니다.")
    private Long currentBidPrice;

    @Schema(description = "포함된 상품 사진들입니다.")
    private List<String> imageUrls;

    private Boolean isLiked;

    //실시간 남은 시간 계산용
    @JsonIgnore
    public Long getRemainingTimeInSeconds() {
        if (actionEndTime == null) return 0L;
        return Duration.between(LocalDateTime.now(),actionEndTime).toSeconds();
    }


}
