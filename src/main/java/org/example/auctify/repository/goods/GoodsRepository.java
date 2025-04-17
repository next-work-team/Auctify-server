package org.example.auctify.repository.goods;

import org.example.auctify.dto.bid.BidSummaryDTO;
import org.example.auctify.entity.Goods.GoodsEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GoodsRepository extends JpaRepository<GoodsEntity, Long>,GoodsRepositoryCustom {


    //JPQL에서 JOIN FETCH를 사용할 경우 COUNT(*) 같은
    // 카운트 쿼리를 자동으로 생성할 수 없기 때문에 쿼리 2개 사용하거나 또는
    // EntityGraph 사용 필수
    @EntityGraph(attributePaths = {
            "user"
            ,"bidHistories"})
    @Query("SELECT g FROM GoodsEntity g WHERE g.user.userId = :userId" +
            " Order BY g.goodsId DESC")
    Page<GoodsEntity> findGoodsByUserId(@Param("userId") Long userId, Pageable pageable);


    @EntityGraph(attributePaths = {
            "bidHistories"
    })
    @Query("SELECT g FROM GoodsEntity g where g.goodsId = :goodsId")
    Optional<GoodsEntity> findGoodsBidHistoryByGoodsId(Long goodsId);

    @Query("SELECT new org.example.auctify.dto.bid.BidSummaryDTO(" +
            "b.bidHistoryId, b.bidPrice, b.createdAt) " +
            "FROM BidHistoryEntity b " +
            "WHERE b.goods.goodsId = :goodsId " +
            "ORDER BY b.createdAt DESC")
    List<BidSummaryDTO> findTopBidHistoryByGoodsId(
            @Param("goodsId") Long goodsId,
            Pageable pageable
    );

    @EntityGraph(attributePaths = {
            "bidHistories"})
    @Query("SELECT g FROM GoodsEntity g " +
            "WHERE g.goodsProcessStatus = org.example.auctify.dto.Goods.GoodsProcessStatus.BIDDING " +
            "AND g.actionEndTime < CURRENT_TIMESTAMP")
    List<GoodsEntity> findExpiredBiddingGoods();

    @Query("SELECT g FROM GoodsEntity g " +
            "WHERE g.goodsProcessStatus = org.example.auctify.dto.Goods.GoodsProcessStatus.BIDDING " +
            "AND g.actionEndTime - CURRENT_TIMESTAMP = 1")
    List<GoodsEntity> findDeadlineBiddingGoods();
}
