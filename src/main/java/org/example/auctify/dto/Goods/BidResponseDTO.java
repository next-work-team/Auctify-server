package org.example.auctify.dto.Goods;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

/**
 * worker : 조영흔
 * work : 입찰을 응답하는 데이터가 담긴 DTO
 * date    : 2025/03/12
 */
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonFormat(shape = JsonFormat.Shape.STRING)
public class BidResponseDTO {

    @Schema(description = "경매 id")
    private Long goodsId;

    @Schema(description = "나의 입찰 금액")
    private Long bidPrice;

    @Schema(description = "입찰 등록된 시간")
    private LocalDateTime bidTime;  // 입찰 시간을 나타내는 LocalDateTime 필드 추가
}
