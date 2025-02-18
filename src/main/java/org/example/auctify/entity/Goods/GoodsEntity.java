package org.example.auctify.entity.Goods;


import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.apache.catalina.User;
import org.example.auctify.entity.BaseTimeEntity;
import org.example.auctify.entity.user.UserEntity;

import java.time.LocalDateTime;

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
public class GoodsEntity extends BaseTimeEntity {
    //경매 등록에 있어 필요값은 다음과 같습니다.
    //경매제목, 설명, 최소입찰금액, 상품상태(2중 택1), 경매 종료시간 또한 필요합니다.
    //즉시구매가는 옵셔널이며, 원화단위 제한있음.

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "goods_id")
    private Long goodsId;

    @Column(name="goods_name", nullable = false)
    @Size(min=1, max=10)
    private String goodsName;

    @Column(name="goods_description", nullable = false, length = 2000)
    @Size(min=20, max=2000)
    private String goodsDescription;

    @Column(name="category_type", nullable = false)
    private String categoryType;

    @Column(name="buy_now_price")
    private Long buyNowPrice;

    @Column(name="goods_process_status")
    private String goodsProcessStatus;

    @Column(name="goods_status", nullable = false)
    private String goodsStatus;

    @Column(name="minimum_bid_amount", nullable = false)
    private Long minimumBidAmount;

    //BaseTimeEntity 의 Created_at 이 경매 시작시간이 되게 됩니다.
    //경매 종료 시간만 입력.
    @Column(name="action_endtime" , nullable = false)
    private LocalDateTime actionEndTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name="category_id")
    private GoodsCategoryEntity categoryId;

}
