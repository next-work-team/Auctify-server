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

public interface BidHistoryRepository extends JpaRepository<BidHistoryEntity, Long> {


    //JPQL에서 JOIN FETCH를 사용할 경우 COUNT(*) 같은
    // 카운트 쿼리를 자동으로 생성할 수 없기 때문에 쿼리 2개 사용하거나 또는
    // EntityGraph 사용 필수
    @EntityGraph(attributePaths = {
            "goods",
            "goods.image",
            "goods.bidHistories"
    })
    @Query("""
    SELECT b
    FROM BidHistoryEntity b
    JOIN FETCH b.goods g
    LEFT JOIN FETCH g.image gi
    WHERE b.user.userId = :userId
    ORDER BY b.bidHistoryId DESC
    """)
    Page<BidHistoryEntity> findBidHistoryByUserId(@Param("userId") Long userId, Pageable pageable);

    Optional<BidHistoryEntity> findFirstByGoodsOrderByCreatedAt(GoodsEntity goods);

    Optional<BidHistoryEntity> findByGoodsAndBidStatus(GoodsEntity goods, Boolean BidStatus);

    List<BidHistoryEntity> findByGoods(GoodsEntity goods);
}
