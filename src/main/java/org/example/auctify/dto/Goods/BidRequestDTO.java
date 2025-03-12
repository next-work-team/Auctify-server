package org.example.auctify.dto.Goods;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
/**
 * worker : 조영흔
 * work : 입찰을 요청하는 데이터가 담긴 DTO
 * date    : 2025/03/12
 */
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonFormat(shape = JsonFormat.Shape.STRING)
public class BidRequestDTO {

    @Schema(description = "경매 id")
    private Long goodsId;

    @Schema(description = "나의 입찰된 금액")
    private Long bidPrice;
}
