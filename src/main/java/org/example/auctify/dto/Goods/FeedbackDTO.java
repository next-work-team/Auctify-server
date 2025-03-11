package org.example.auctify.dto.Goods;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;


@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class FeedbackDTO {


    @Schema(description = "경매 등록자")
    private String registerNickname;

    @Schema(description = "낙찰자")
    private String bidderNickName;

    @Schema(description = "후기내용")
    private String content;

    @Schema(description = "경매품 아이디")
    private long goodsId;

    @Schema(description = "경매품 이미지")
    private String auctionImage;

    @Schema(description = "경매 평점")
    private double score;

}
