package org.example.auctify.repository.goods;

import org.example.auctify.entity.Goods.GoodsEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GoodsRepository extends JpaRepository<GoodsEntity, Long> {


    //JPQL에서 JOIN FETCH를 사용할 경우 COUNT(*) 같은
    // 카운트 쿼리를 자동으로 생성할 수 없기 때문에 쿼리 2개 사용하거나 또는
    // EntityGraph 사용 필수
    @EntityGraph(attributePaths = {
            "user"
            ,"image"
            ,"bidHistories"})
    @Query("SELECT g FROM GoodsEntity g WHERE g.user.userId = :userId" +
            " Order BY g.goodsId DESC")
    Page<GoodsEntity> findGoodsByUserId(@Param("userId") Long userId, Pageable pageable);

}
