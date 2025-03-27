package org.example.auctify.entity.bidHistory;

import jakarta.persistence.*;
import lombok.*;
import org.example.auctify.entity.BaseTimeEntity;
import org.example.auctify.entity.Goods.GoodsEntity;
import org.example.auctify.entity.payment.PaymentEntity;
import org.example.auctify.entity.user.UserEntity;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(name = "bid_history")
public class BidHistoryEntity extends BaseTimeEntity {


    // 경매 참여 내역 ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bidHistory_id")
    private Long bidHistoryId;

    // 압찰 취소 여부
    //boolean 타입은 **BIT(1)**로 저장됩니다.
    // **BIT(1)**은 0 또는 1을 저장할 수 있는 1비트 크기의 컬럼 타입입니다.
    @Column(name = "cancel_flag")
    private Boolean cancelFlag;

    // 입찰 금액
    @Column(name = "bid_price")
    private Long bidPrice;

    //낙찰 여부
    @Column(name = "bid_status")
    private Boolean bidStatus;


    //경매 참여자 ID 현 엔티티가 왜래키 주인
    @ManyToOne(fetch = FetchType.LAZY)         // 다대일 관계를 나타냄
    @JoinColumn(name = "bid_user_Id")          // UserEntity와 bid_user_Id로 연결
    private UserEntity user;

    //경매 물품정보 ID  현 엔티티가 외래키의 주인
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goods_id")             // GoodsEntity goods_id로  연결
    private GoodsEntity goods;

    // 추가된 부분 (양방향 매핑)
    @OneToOne(mappedBy = "bidHistory", fetch = FetchType.LAZY)
    private PaymentEntity payment;


    public void onChangeCancelFlag(boolean flag) {
        this.cancelFlag = flag;
    }

}
