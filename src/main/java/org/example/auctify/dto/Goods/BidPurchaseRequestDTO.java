package org.example.auctify.dto.Goods;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.example.auctify.dto.user.AddressDTO;

/**
 * worker : 조영흔
 * work : 낙찰 후 결제에 등록된 결제정보 대한 응답 DTO
 * date    : 2025/03/12
 */
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonFormat(shape = JsonFormat.Shape.STRING)
public class BidPurchaseRequestDTO {


    @Schema(description = "경매 등록자")
    private String registerNickname;

    @Schema(description = "경매 등록자 아이디")
    private Long registerId;

    @Schema(description = "경매 낙찰자 본인")
    private String biderNickname;

    @Schema(description = "경매 낙찰자 본인 Id")
    private Long biderId;

    @Schema(description = "경매 id")
    private Long goodsId;

    @Schema(description = "입찰 id")
    private Long bidId;

    @Schema(description = "경매품 이름")
    private String goodsName;

    @Schema(description = "나의 결제금액")
    private double bidPrice;

    @Schema(description = "나의 결제 수단")
    private double paySort;

    @Schema(description = "선택된 주소")
    AddressDTO addressDTO;
}
