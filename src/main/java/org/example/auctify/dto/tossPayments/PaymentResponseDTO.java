package org.example.auctify.dto.tossPayments;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.example.auctify.dto.Goods.GoodsCategory;
import org.example.auctify.dto.Goods.GoodsStatus;
import org.example.auctify.dto.user.AddressDTO;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResponseDTO {

    @Schema(description = "결제 Id")
    private Long paymentId;

    @Schema(description = "결제 유형")
    private String type;

    @Schema(description = "경매 물품 이름")
    private String goodsName;

    @Schema(description = "경매 상태")
    private GoodsStatus goodsStatus;

    @Schema(description = "경매 카테고리")
    private GoodsCategory goodsCategory;

    @Schema(description = "경매 첫번째 이미지")
    private String goodsImage;

    @Schema(description = "결제 금액")
    private Long amount;

    @Schema(description = "경매 등록자 ID")
    private Long goodsUserId;

    @Schema(description = "배송지")
    private AddressDTO addressDTO;

}
