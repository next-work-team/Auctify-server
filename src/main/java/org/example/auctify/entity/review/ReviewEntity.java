package org.example.auctify.entity.review;

import jakarta.persistence.*;
import lombok.*;
import org.example.auctify.entity.BaseTimeEntity;
import org.example.auctify.entity.Goods.GoodsEntity;
import org.example.auctify.entity.bidHistory.BidHistoryEntity;
import org.example.auctify.entity.payment.PaymentEntity;
import org.example.auctify.entity.user.UserEntity;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(name = "review")
public class ReviewEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    @Column(name = "content")
    private String content;

    @Column(name = "temperature")
    private Double temperature;

    //리뷰를 받은 사람
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_receiver_id")
    private UserEntity receiverUser;

    //리뷰를 작성한 사람
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_writer_id")
    private UserEntity writerUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id") // 외래키를 소유 (외래키의 주인)
    private PaymentEntity payment;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goods_id")
    private GoodsEntity goods;
}
