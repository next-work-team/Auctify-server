package org.example.auctify.dto.Goods;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

/**
 * worker : 조영흔
 * work : 내가 입찰한 정보를 반환하는 DTO
 * date    : 2025/03/12
 */
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonFormat(shape = JsonFormat.Shape.STRING)
public class AuctionResponseGoodsDTO {

    @Schema(description = "경매 등록자")
    private String registerNickname;

    @Schema(description = "경매 등록자 아이디")
    private Long registerId;

    @Schema(description = "경매 입찰자 본인")
    private String biderNickname;

    @Schema(description = "경매 입찰자 본인 Id")
    private Long biderId;

    @Schema(description = "경매 id")
    private Long goodsId;

    @Schema(description = "경매품 이미지")
    private List<String> goodsImage;

    @Schema(description = "경매품 이름")
    private String goodsName;

    @Schema(description = "나의 입찰가")
    private double BidPrice;

    @Schema(description = "현재 최고 입찰가")
    private double MaxBidPrice;

    @Schema(description = "경매 낙찰 여부")
    private GoodsStatus goodsStatus;

    @Schema(description = "경매 낙찰자 ID 없거나 진행중이면 빈 문자열")
    private String auctionId;

}
