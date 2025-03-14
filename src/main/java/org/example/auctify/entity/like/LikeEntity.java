package org.example.auctify.entity.like;


import jakarta.persistence.*;
import lombok.*;
import org.example.auctify.entity.BaseTimeEntity;
import org.example.auctify.entity.Goods.GoodsEntity;
import org.example.auctify.entity.user.UserEntity;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity(name = "likes")
public class LikeEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_id")
    private Long likeId;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goods_id")
    private GoodsEntity goods;
}
