package org.example.auctify.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MannerTemperatureDTO {


    @Schema(description = "경매 등록자")
    private String registerNickname;

    @Schema(description = "경매 등록자 Id")
    private Long registerId;

    @Schema(description = "낙찰자")
    private String bidderNickName;

    @Schema(description = "경매 입찰자 Id")
    private String bidderId;


    @Schema(description = "후기내용")
    private String content;

    @Schema(description = "경매품 이미지")
    private String auctionImage;

    @Schema(description = "경매 평점")
    private Double score;
}
