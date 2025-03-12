package org.example.auctify.dto.Goods;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * worker : 조영흔
 * work : 경매 검색 정보에 대한 DTO
 * date    : 2025/03/12
 */
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonFormat(shape = JsonFormat.Shape.STRING)
public class SearchDTO {

    @Schema(description = "경매 제목")
    private String auctionTitle;    // 경매 제목

    @Schema(description = "경매 현재 입찰가")
    private double currentBidPrice; // 현재 입찰가

    @Schema(description = "경매 즉시 구매가")
    private double buyNowPrice;     // 즉시구매가

    @Schema(description = "입찰 횟수")
    private int bidCount;           // 입찰 횟수

    @Schema(description = "최소 남은 경매 시간" +
            "{\n" +
            "  \"remainingTime\": \"PT1H\"\n" +
            "} = 1시간")
    private Duration remainingTime;     // 남은 경매 시간 (Duration 객체로 시간, 분, 초 등 다룰 수 있음)

    @Schema(description = "새상품 | 중고 여부")
    private boolean isNew;          // 상품 상태 (새상품 | 중고)

    @Schema(description = "좋아요 여부")
    private boolean isLiked;        // 좋아요 여부 (필터에서 사용)
}
