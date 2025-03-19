package org.example.auctify.dto.like;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

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
}
