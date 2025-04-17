package org.example.auctify.repository.goods;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.example.auctify.dto.Goods.GoodsCategory;
import org.example.auctify.dto.Goods.GoodsProcessStatus;
import org.example.auctify.dto.Goods.GoodsResponseSummaryDTO;
import org.example.auctify.dto.Goods.GoodsStatus;
import org.example.auctify.entity.Goods.QGoodsEntity;
import org.example.auctify.entity.bidHistory.QBidHistoryEntity;
import org.example.auctify.entity.like.QLikeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class GoodsRepositoryCustomImpl implements GoodsRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<GoodsResponseSummaryDTO> searchGoodsWithLikeStatus(
            Long userId,
            String category,
            Double priceRangeLow,
            Double priceRangeHigh,
            String goodsStatus,
            String goodsProcessStatus,
            String goodsName,
            String sort,
            Pageable pageable
    ) {
        QGoodsEntity goods = QGoodsEntity.goodsEntity;
        QBidHistoryEntity bid = QBidHistoryEntity.bidHistoryEntity;
        QLikeEntity like = QLikeEntity.likeEntity;

        BooleanBuilder builder = new BooleanBuilder();

        if (category != null && !category.isEmpty()) {
            builder.and(goods.category.eq(GoodsCategory.valueOf(category)));
        }
        if (priceRangeLow != null) {
            builder.and(goods.buyNowPrice.goe(priceRangeLow));
        }
        if (priceRangeHigh != null) {
            builder.and(goods.buyNowPrice.loe(priceRangeHigh));
        }
        if (goodsStatus != null && !goodsStatus.isEmpty()) {
            builder.and(goods.goodsStatus.eq(GoodsStatus.valueOf(goodsStatus)));
        }
        if (goodsProcessStatus != null && !goodsProcessStatus.isEmpty()) {
            builder.and(goods.goodsProcessStatus.eq(GoodsProcessStatus.valueOf(goodsProcessStatus)));
        }
        if (goodsName != null && !goodsName.isEmpty()) {
            builder.and(goods.goodsName.containsIgnoreCase(goodsName));
        }

        OrderSpecifier<?> orderSpecifier = getSort(goods, bid, sort);

        List<org.example.auctify.entity.Goods.GoodsEntity> entityList = queryFactory
                .selectFrom(goods)
                .leftJoin(like).on(
                        like.goods.goodsId.eq(goods.goodsId)
                                .and(like.user.userId.eq(userId))
                ).fetchJoin()
                .where(builder)
                .orderBy(orderSpecifier)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(goods.count())
                .from(goods)
                .where(builder)
                .fetchOne();

        List<GoodsResponseSummaryDTO> dtoList = entityList.stream().map(g -> GoodsResponseSummaryDTO.builder()
                .goodsId(g.getGoodsId())
                .goodsName(g.getGoodsName())
                .goodsProcessStatus(g.getGoodsProcessStatus())
                .currentBidPrice(g.getCurrentBidPrice())
                .imageUrls(g.getFirstImage())
                .endTime(g.getActionEndTime())
                .category(g.getCategory())
                .goodsStatus(g.getGoodsStatus())
                .currentBidCount((long) g.getBidHistories().size())
                .isLiked(
                        g.getLike().stream().anyMatch(l -> l.getUser().getUserId().equals(userId))
                )
                .build()
        ).collect(Collectors.toList());

        return new PageImpl<>(dtoList, pageable, total);
    }

    private OrderSpecifier<?> getSort(QGoodsEntity goods, QBidHistoryEntity bid, String sort) {
        if ("priceAsc".equalsIgnoreCase(sort)) {
            return goods.buyNowPrice.asc();
        } else if ("priceDesc".equalsIgnoreCase(sort)) {
            return goods.buyNowPrice.desc();
        } else if ("currentPriceAsc".equalsIgnoreCase(sort)) {
            return currentBidPriceExpr(goods, bid).asc();
        } else if ("currentPriceDesc".equalsIgnoreCase(sort)) {
            return currentBidPriceExpr(goods, bid).desc();
        } else if ("latest".equalsIgnoreCase(sort)) {
            return goods.createdAt.desc();
        }
        return goods.createdAt.desc();
    }

    private NumberExpression<Long> currentBidPriceExpr(QGoodsEntity goods, QBidHistoryEntity bid) {
        NumberExpression<Long> maxBidPrice = Expressions.numberTemplate(Long.class,
                "(select coalesce(max(b.bidPrice), 0) from BidHistoryEntity b where b.goods.goodsId = {0})",
                goods.goodsId
        );

        return new CaseBuilder()
                .when(maxBidPrice.gt(goods.minimumBidAmount)).then(maxBidPrice)
                .otherwise(goods.minimumBidAmount);
    }
}