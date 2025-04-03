


package org.example.auctify.dto.tossPayments;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.STRING)
public class PayRequestDTO {

    @Schema(description = "토스로 받은 (의미 x, 임의의 값)")
    String paymentKey;
    @Schema(description = "주문 아이디 만들어진 주문 ID (의미 x, 임의의 값) ")
    String orderId;
    @Schema(description = "결제 총액 결제 값 보내주면 됨")
    Long amount;
    @Schema(description = "결제 type 클라이언트에서 (전달 안 해주는게 맞음, 서버에서 나중에 채움)")
    String type;
    @Schema(description = "결제자 아이디 필수")
    Long bidId;
    @Schema(description = "경매 상품 굳즈 아이디 필수")
    Long goodsId;
    @Schema(description = "유저 아이디 필수")
    Long userId;
    @Schema(description = "주소 아이디 필수")
    Long addressId;
}
