package org.example.auctify.repository.bidHistory;

import java.util.List;
import java.util.Optional;
import org.example.auctify.dto.bid.BidHistoryResponseDTO;
import org.example.auctify.entity.Goods.GoodsEntity;
import org.example.auctify.entity.bidHistory.BidHistoryEntity;
import org.example.auctify.entity.user.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BidHistoryRepository extends JpaRepository<BidHistoryEntity, Long>,BidHistoryRepositoryCustom {


    Optional<BidHistoryEntity> findFirstByGoodsOrderByCreatedAtDesc(GoodsEntity goods);

    Optional<BidHistoryEntity> findByGoodsAndBidStatus(GoodsEntity goods, Boolean BidStatus);

    List<BidHistoryEntity> findByGoods(GoodsEntity goods);
}
