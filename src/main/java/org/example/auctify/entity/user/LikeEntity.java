package org.example.auctify.entity.user;


import jakarta.persistence.*;
import lombok.*;
import org.example.auctify.entity.Goods.GoodsEntity;

/**
 * worker : 박예빈
 * work : 유저와 연결된 찜 매핑 테이블
 * date    : 2025/03/01~
 */

@Entity(name="like")
@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class LikeEntity {

    @Id
    @GeneratedValue
    @Column(name="likeId")
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private UserEntity userId;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(nullable = false)
    private GoodsEntity goodsId;

}
