package org.example.auctify.dto.notification;

import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder
public class BidUpdateResDTO {
	private Long goodsId;
	private List<BidHistoryDTO> bidHistory;
	private AuctionSummaryDTO auctionSummary;
}


