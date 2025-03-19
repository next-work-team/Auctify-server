package org.example.auctify.dto.Goods;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;


@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class ReviewDetailResponseDTO {

    @Schema(description = "받은사람 Id")
    private Long userId;

    @Schema(description = "경매 등록자 닉네임")
    private String registerNickname;

    @Schema(description = "낙찰자 닉네임")
    private String bidderNickName;


    @Schema(description = "경매 등록자 Id")
    private Long registerId;

    @Schema(description = "낙찰자 Id")
    private Long bidderId;

    @Schema(description = "후기내용")
    private String content;

    @Schema(description = "경매품 아이디")
    private long goodsId;

    @Schema(description = "경매품 이미지")
    private String goodsImage;

    @Schema(description = "경매 평점")
    private Double temperature;

}
