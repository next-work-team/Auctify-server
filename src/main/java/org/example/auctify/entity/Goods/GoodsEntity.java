package org.example.auctify.entity.Goods;


import jakarta.persistence.*;
import lombok.*;

/**
 * worker : 박예빈
 * work : 경매물품(Goods)에 관한 태이블을 생성해주는 엔티티 클라스 작업중
 * date    : 2025/02/18~
 */


@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class GoodsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "goods_id")
    private Long goodsId;

    @Column(name="goods_name", nullable = false, length = 10)
    private String goodsName;

    @Column(name="goods_description", nullable = false, length = 2000)
    private String goodsDescription;

    @Column(name="first_price", nullable = false)
    private Long goodsPrice;
}
