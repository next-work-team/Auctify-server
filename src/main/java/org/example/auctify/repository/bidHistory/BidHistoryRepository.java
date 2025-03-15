package org.example.auctify.repository.bidHistory;

import org.example.auctify.entity.bidHistory.BidHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BidHistoryRepository extends JpaRepository<BidHistoryEntity, Long> {
}
