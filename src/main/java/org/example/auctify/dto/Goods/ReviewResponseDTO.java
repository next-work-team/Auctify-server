package org.example.auctify.dto.Goods;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

/**
 * worker : 조영흔
 * work : 후기 응답 DTO
 * date    : 2025/03/12
 */

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonFormat(shape = JsonFormat.Shape.STRING)
public class ReviewResponseDTO {

    @Schema(description = "경매 id")
    private Long goodsId;

    @Schema(description = "구매 id")
    private Long paymentId;

    @Schema(description = "후기 내용")
    private String content;

    @Schema(description = "매너 온도")
    private Double temperature;

    @Schema(description = "리뷰 등록 시간")
    private LocalDateTime time;  // 입찰 시간을 나타내는 LocalDateTime 필드 추가
}
