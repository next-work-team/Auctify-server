package org.example.auctify.dto.Goods;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;


// 내가 입찰 낙찰 한 정보를 저장하는 DTO
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@JsonFormat(shape = JsonFormat.Shape.STRING)
public class AuctionGoodsDTO {

    @Schema(description = "경매 등록자")
    private String registerNickname;

    @Schema(description = "경매 입찰자 본인")
    private String biderNickname;

    @Schema(description = "경매 id")
    private String goodsId;

    @Schema(description = "경매품 이미지")
    private List<String> auctionImage;

    @Schema(description = "경매품 이름")
    private String goodsName;

    @Schema(description = "나의 입찰가")
    private double myBidAmount;

    @Schema(description = "현재 최고 입찰가")
    private double MaxBidAmount;

    @Schema(description = "경매 낙찰 여부")
    private GoodsStatus goodsStatus;

}
