package org.example.auctify.repository.bidHistory;

import org.example.auctify.entity.bidHistory.BidHistoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BidHistoryRepositoryCustom {

    Page<BidHistoryEntity> searchMyBid(
            Long userId,
            String category,
            Pageable pageable);
}
