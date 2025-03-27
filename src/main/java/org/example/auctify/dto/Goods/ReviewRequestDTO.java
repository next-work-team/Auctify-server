package org.example.auctify.dto.Goods;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

/**
 * worker : 조영흔
 * work : 후기 요청 DTO
 * date    : 2025/03/12
 */
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonFormat(shape = JsonFormat.Shape.STRING)
public class ReviewRequestDTO {

    @Schema(description = "경매 id")
    private Long goodsId;

    @Schema(description = "구매 id")
    private Long paymentId;

    @Schema(description = "리뷰 받는 유저 id")
    private Long receiver;

    @Schema(description = "후기 내용")
    private String content;

    @Schema(description = "매너 온도")
    private Double temperature;

    @Schema(description = "입찰 등록된 시간")
    private LocalDateTime bidTime;  // 입찰 시간을 나타내는 LocalDateTime 필드 추가
}
