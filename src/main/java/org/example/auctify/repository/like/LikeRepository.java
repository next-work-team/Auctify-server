package org.example.auctify.repository.like;

import org.example.auctify.entity.Goods.GoodsEntity;
import org.example.auctify.entity.like.LikeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<LikeEntity, Long> {


    @EntityGraph(attributePaths = {
            "user",          // 좋아요 한 사용자 정보
            "goods",         // 연결된 상품 정보
            "goods.image"    // 상품의 대표 이미지 정보 (OneToMany 관계)
    })
    @Query("SELECT l FROM LikeEntity l WHERE l.goods.user.userId = :userId" +
            " Order BY l.likeId DESC")
    Page<LikeEntity> findLikeGoodsByUserId(@Param("userId") Long userId, Pageable pageable);

    Optional<LikeEntity> findByUser_UserIdAndGoods_GoodsId(Long userId, Long goodsId);


    boolean existsByUserUserIdAndGoodsGoodsId(Long userId, long goodsId);
}
