package org.example.auctify.repository.bidHistory;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.example.auctify.dto.Goods.GoodsProcessStatus;
import org.example.auctify.entity.Goods.QGoodsEntity;
import org.example.auctify.entity.Goods.QGoodsImageEntity;
import org.example.auctify.entity.bidHistory.BidHistoryEntity;
import org.example.auctify.entity.bidHistory.QBidHistoryEntity;
import org.example.auctify.entity.payment.QPaymentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RequiredArgsConstructor
public class BidHistoryRepositoryImpl implements BidHistoryRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<BidHistoryEntity> searchMyBid(Long userId, String category, Pageable pageable) {
        QGoodsEntity goods = QGoodsEntity.goodsEntity;
        QBidHistoryEntity bid = QBidHistoryEntity.bidHistoryEntity;
        QGoodsImageEntity goodsImage = QGoodsImageEntity.goodsImageEntity;
        QPaymentEntity payment = QPaymentEntity.paymentEntity;
        BooleanBuilder builder = new BooleanBuilder();

        builder.and(bid.user.userId.eq(userId));

        if (category != null && !category.isEmpty()) {
            if ("BIDDING".equals(category)) {
                builder.and(goods.goodsProcessStatus.eq(GoodsProcessStatus.valueOf(category)));
            }
            if (category.startsWith("AWARDED")){
                builder.and(goods.goodsProcessStatus.eq(GoodsProcessStatus.valueOf("AWARDED")));

                if("AWARDED_WIN".equals(category)){
                    builder.and(bid.bidStatus.eq(true));
                }
                if("AWARDED_LOSE".equals(category)){
                    builder.and(bid.bidStatus.eq(false));
                }
            }
        }

        // 콘텐츠 쿼리
        List<BidHistoryEntity> content = queryFactory
                .select(bid)
                .distinct()
                .from(bid)
                //.join(bid.user).on(bid.user.userId.eq(userId))
                .join(bid.user)
                .join(bid.goods, goods).fetchJoin()
                .leftJoin(goods.images, goodsImage).fetchJoin()
                .leftJoin(bid.payment, payment).fetchJoin()
                .where(builder)
                .orderBy(bid.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 카운트 쿼리 (성능 최적화용 fetchJoin 제거)
        Long total = queryFactory
                .select(bid.count())
                .from(bid)
                //.join(bid.user).on(bid.user.userId.eq(userId))
                .join(bid.user)
                .join(bid.goods, goods)
                .leftJoin(bid.payment, payment)
                .where(builder)
                .fetchOne();

        if(total == null){
            total = 0L;
        }

        return new PageImpl<>(content, pageable, total);
    }
}
