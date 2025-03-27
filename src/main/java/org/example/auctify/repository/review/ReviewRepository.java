package org.example.auctify.repository.review;

import org.example.auctify.entity.review.ReviewEntity;
import org.example.auctify.entity.user.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface ReviewRepository  extends JpaRepository<ReviewEntity, Long> {

    //JPQL에서 JOIN FETCH를 사용할 경우 COUNT(*) 같은
    // 카운트 쿼리를 자동으로 생성할 수 없기 때문에 쿼리 2개 사용하거나 또는
    // EntityGraph 사용 필수
    @EntityGraph(attributePaths = {
            "receiverUser",
            "writerUser",
            "payment",
            "goods",
            "goods.images"
    })
    @Query("SELECT r FROM ReviewEntity r WHERE r.receiverUser = :receiverUser ORDER BY r.createdAt DESC")
    Page<ReviewEntity> findReviewsByReceiverUser(@Param("receiverUser") UserEntity receiverUser, Pageable pageable);


    Optional<ReviewEntity> findByWriterUser_UserIdAndGoods_GoodsId(Long userId, Long goodsId);






}
