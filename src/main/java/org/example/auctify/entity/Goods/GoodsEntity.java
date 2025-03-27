package org.example.auctify.entity.Goods;


import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import net.minidev.json.annotate.JsonIgnore;
import org.example.auctify.dto.Goods.GoodsCategory;
import org.example.auctify.dto.Goods.GoodsProcessStatus;
import org.example.auctify.dto.Goods.GoodsStatus;
import org.example.auctify.entity.BaseTimeEntity;
import org.example.auctify.entity.bidHistory.BidHistoryEntity;
import org.example.auctify.entity.like.LikeEntity;
import org.example.auctify.entity.user.UserEntity;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.NumberExpression;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * worker : 박예빈
 * work : 경매물품(Goods)에 관한 태이블을 생성해주는 엔티티 클라스 작업중
 * date    : 2025/02/18~
 */


@Getter
@Entity
@Table(name = "goods")
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

    @Column(name = "goods_name", nullable = false)
    @Size(min = 1, max = 10)
    private String goodsName;

    @Column(name = "goods_description", nullable = false, length = 2000)
    @Size(min = 20, max = 2000)
    private String goodsDescription;

    @Column(name = "buy_now_price")
    private Long buyNowPrice;

    // 상품 상태 new 중고
    @Column(name = "goods_status")
    private GoodsStatus goodsStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "goods_process_status", nullable = false)
    private GoodsProcessStatus goodsProcessStatus;

    // 최소 입찰 가격
    @Column(name = "minimum_bid_amount", nullable = false)
    private Long minimumBidAmount;

    //BaseTimeEntity 의 Created_at 이 경매 시작시간이 되게 됩니다.
    //경매 종료 시간만 입력.
    @Column(name = "action_endtime", nullable = false)
    private LocalDateTime actionEndTime;

    // 경매를 등록한 유저
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private UserEntity user;


    @Enumerated(EnumType.STRING)
    @Column(name = "goods_category", nullable = false)
    private GoodsCategory category;

    @OneToMany(mappedBy = "goods", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @JsonIgnore
    private List<BidHistoryEntity> bidHistories = new ArrayList<BidHistoryEntity>();

    @OneToMany(mappedBy = "goods", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @JsonIgnore
    private List<LikeEntity> like = new ArrayList<LikeEntity>();

    @OneToMany(mappedBy = "goods", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @JsonIgnore
    private List<GoodsImageEntity> image = new ArrayList<GoodsImageEntity>();


    public String getFirstImage(){
        String firstImage = "";
        if (!Objects.isNull(image) && !image.isEmpty()) {
            firstImage = image.get(0).getImageSrc();
        }
        return firstImage;
    }

    public Long getMaxBidPrice(){
        return bidHistories.stream()
                .mapToLong(BidHistoryEntity::getBidPrice)
                .max()
                .orElse(0);

    }

    public Long getCurrentBidPrice(){
        // 현재 입찰가
        Long currentPrice;
        currentPrice = getMaxBidPrice()
                > minimumBidAmount ? getMaxBidPrice() : minimumBidAmount;
        return currentPrice;
    }


    public List<String> getImageUrls() {
        if (image == null || image.isEmpty()) {
            return Collections.emptyList(); // 빈 리스트 반환 권장 (null보다는 빈 리스트)
        }

        return image.stream()
                .map(img -> img.getImageSrc())
                .collect(Collectors.toList());
    }



}
