package org.example.auctify.entity.payment;

import jakarta.persistence.*;
import lombok.*;
import net.minidev.json.annotate.JsonIgnore;
import org.example.auctify.entity.BaseTimeEntity;
import org.example.auctify.entity.Goods.GoodsEntity;
import org.example.auctify.entity.bidHistory.BidHistoryEntity;
import org.example.auctify.entity.review.ReviewEntity;
import org.example.auctify.entity.user.AddressEntity;
import org.example.auctify.entity.user.UserEntity;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(name = "payment")
public class PaymentEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long paymentId;

    //결제 방식
    @Column(name = "type")
    private String type;

    //총액
    @Column(name = "amount")
    private Long amount;

    //결제 유저
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_user_id")
    private UserEntity user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bidHistory_id") // 외래키를 소유 (외래키의 주인)
    private BidHistoryEntity bidHistory;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id")
    private AddressEntity address;


    // 연관관계의 주인은 ReviewEntity 결제가 수정삭제될 때 리뷰도 영향을 받음
    // JsonIgnore > 순환참조 문제를 해결 한쪽에만 적어주면 된다.
    @OneToMany(mappedBy = "payment", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @JsonIgnore
    private List<ReviewEntity> reviewEntityList = new ArrayList<ReviewEntity>();


}
