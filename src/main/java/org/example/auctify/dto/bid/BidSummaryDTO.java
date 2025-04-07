package org.example.auctify.dto.bid;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BidSummaryDTO {
    @Schema(description = "입찰id")
    private Long bidHistoryId;

    @Schema(description = "입찰가")
    private Long bidPrice;

    @Schema(description = "입찰 날짜")
    LocalDateTime createdAt;


}
