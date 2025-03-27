package org.example.auctify.repository.goods;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.example.auctify.dto.Goods.GoodsCategory;
import org.example.auctify.dto.Goods.GoodsProcessStatus;
import org.example.auctify.dto.Goods.GoodsStatus;
import org.example.auctify.entity.Goods.GoodsEntity;
import org.example.auctify.entity.Goods.QGoodsEntity;
import org.example.auctify.entity.bidHistory.QBidHistoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

/**
 * 커스텀 검색 기능 구현체
 * QueryDSL을 이용해 필터링/정렬/페이지네이션이 가능한 상품 검색 쿼리 작성
 */
@RequiredArgsConstructor
// QueryDSL 을 사용하려면 interface -> impl 구조로 만들어야한다
public class GoodsRepositoryCustomImpl implements GoodsRepositoryCustom {

    private final JPAQueryFactory queryFactory; // QueryDSL 쿼리 생성기 (생성자 주입)

    @Override
    public Page<GoodsEntity> searchGoods(String category, Double priceRangeLow,
                                         Double priceRangeHigh, String goodsStatus,
                                         String goodsProcessStatus,
                                         String goodsName, String sort, Pageable pageable) {

        QGoodsEntity goods = QGoodsEntity.goodsEntity; // QueryDSL용 Q타입
        QBidHistoryEntity bid = QBidHistoryEntity.bidHistoryEntity;

        BooleanBuilder builder = new BooleanBuilder(); // 동적 where 조건 빌더

        // 카테고리 필터
        if (category != null && !category.isEmpty()) {
            GoodsCategory goodsCategory = GoodsCategory.valueOf(category);
            builder.and(goods.category.eq(goodsCategory));
        }

        // 즉시 구매가 하한 필터
        if (priceRangeLow != null) {
            builder.and(goods.buyNowPrice.goe(priceRangeLow));
        }

        // 즉시 구매가 상한 필터
        if (priceRangeHigh != null) {
            builder.and(goods.buyNowPrice.loe(priceRangeHigh));
        }

        // 상품 상태 필터 (e.g., NEW, USED)
        if (goodsStatus != null && !goodsStatus.isEmpty()) {
            GoodsStatus goodsStatusEnum = GoodsStatus.valueOf(goodsStatus);
            builder.and(goods.goodsStatus.eq(goodsStatusEnum));
        }

        // 경매 상태 필터 (e.g., BIDDING, ENDED)
        if (goodsProcessStatus != null && !goodsProcessStatus.isEmpty()) {
            GoodsProcessStatus goodsProcessStatusEnum = GoodsProcessStatus.valueOf(goodsProcessStatus);
            builder.and(goods.goodsProcessStatus.eq(goodsProcessStatusEnum));
        }

        // 상품명 검색 (대소문자 무시)
        if (goodsName != null && !goodsName.isEmpty()) {
            builder.and(goods.goodsName.containsIgnoreCase(goodsName));
        }

        // 정렬 조건 생성 (문자열로 받은 정렬 키워드에 따라 동적으로 결정)
        OrderSpecifier<?> orderSpecifier = getSort(goods, bid, sort);

        // 실제 데이터 조회 쿼리 (페이징 적용)
        List<GoodsEntity> result = queryFactory.selectFrom(goods)
                .where(builder)
                .orderBy(orderSpecifier)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 전체 개수 쿼리 (페이지네이션용 total count)
        Long total = queryFactory.select(goods.count())
                .from(goods)
                .where(builder)
                .fetchOne();

        // 스프링에서 Page 객체 생성 (count 최적화 포함)
        return PageableExecutionUtils.getPage(result, pageable, () -> total);
    }

    /**
     * 정렬 키워드에 따라 정렬 조건 반환
     * - priceAsc: 즉시구매가 오름차순
     * - priceDesc: 즉시구매가 내림차순
     * - currentPriceAsc/Desc: 현재 입찰가 기준 정렬
     * - latest: 등록 최신순
     */
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
            return goods.createdAt.desc(); // 최신 등록순
        }
        return goods.createdAt.desc(); // 기본 정렬: 최신순
    }

    /**
     * 현재 입찰가 계산 Expression 반환
     * - 현재 입찰가 = max(bid_price) > minimumBidAmount ? max(bid_price) : minimumBidAmount
     * - max()는 SQL 서브쿼리로 직접 작성
     * - Expressions.numberTemplate()을 통해 QueryDSL 내부 표현 생성
     */
    private NumberExpression<Long> currentBidPriceExpr(QGoodsEntity goods, QBidHistoryEntity bid) {
        // 서브쿼리로 최대 입찰가를 구하고, 없을 경우 0으로 대체
        NumberExpression<Long> maxBidPrice = Expressions.numberTemplate(Long.class,
                "coalesce((select max(b.bid_price) from bid_history b where b.goods_id = {0}), 0)", goods.goodsId);

        // 만약 maxBidPrice > minimumBidAmount 면 그 값을 사용, 아니면 minimumBidAmount 반환
        return new CaseBuilder()
                .when(maxBidPrice.gt(goods.minimumBidAmount)).then(maxBidPrice)
                .otherwise(goods.minimumBidAmount);
    }
}
