package org.example.auctify.repository.goods;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.example.auctify.dto.Goods.GoodsCategory;
import org.example.auctify.dto.Goods.GoodsProcessStatus;
import org.example.auctify.dto.Goods.GoodsResponseSummaryDTO;
import org.example.auctify.dto.Goods.GoodsStatus;
import org.example.auctify.entity.Goods.QGoodsEntity;
import org.example.auctify.entity.Goods.QGoodsImageEntity;
import org.example.auctify.entity.bidHistory.QBidHistoryEntity;
import org.example.auctify.entity.like.QLikeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

@RequiredArgsConstructor
public class GoodsRepositoryCustomImpl implements GoodsRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    /**
     * 상품 목록 조회 - 좋아요 여부와 현재 입찰가 포함
     */
    @Override
    public Page<GoodsResponseSummaryDTO> searchGoodsWithLikeStatus(
            Long userId,                    // 로그인한 사용자 ID (좋아요 여부 확인용)
            String category,                // 카테고리 필터
            Double priceRangeLow,           // 최소 가격 필터
            Double priceRangeHigh,          // 최대 가격 필터
            String goodsStatus,             // 상품 상태 필터 (예: 판매중, 거래완료 등)
            String goodsProcessStatus,      // 상품 프로세스 상태 (예: 경매중, 즉시구매 등)
            String goodsName,               // 상품명 검색
            String sort,                    // 정렬 조건
            Pageable pageable               // 페이징 정보
    ) {

        // Q 클래스들 선언
        QGoodsEntity goods = QGoodsEntity.goodsEntity;
        QBidHistoryEntity bid = QBidHistoryEntity.bidHistoryEntity;
        QLikeEntity like = QLikeEntity.likeEntity;

        QGoodsImageEntity image = QGoodsImageEntity.goodsImageEntity;

        Expression<String> firstImage = JPAExpressions
                .select(image.imageSrc)
                .from(image)
                .where(image.goods.goodsId.eq(goods.goodsId))
                .orderBy(image.imageId.asc()) // 혹은 createdAt.asc()
                .limit(1);

        BooleanBuilder builder = new BooleanBuilder(); // 동적 where 조건 빌더

        // --- 동적 필터 조건들 ---

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

        // 정렬 조건 지정
        OrderSpecifier<?> orderSpecifier = getSort(goods, bid, sort);

        // --- 실제 쿼리 실행 ---

        List<GoodsResponseSummaryDTO> content = queryFactory
                .select(
                        Projections.bean(GoodsResponseSummaryDTO.class, // DTO로 매핑
                                goods.goodsId,
                                goods.goodsName,
                                goods.goodsProcessStatus,

                                // 현재 입찰가 계산 (최고 입찰가 vs 시작가 중 큰 값)
                                currentBidPriceExpr(goods, bid).as("currentBidPrice"),

//                                // 첫 번째 이미지 경로
                                ExpressionUtils.as(firstImage, "imageUrls"),

                                // 경매 종료 시간
                                goods.actionEndTime.as("endTime"),

                                // 카테고리, 상태 정보
                                goods.category,
                                goods.goodsStatus,

                                // 현재 입찰 수 조회 (서브쿼리)
                                ExpressionUtils.as(
                                        JPAExpressions.select(bid.count())
                                                .from(bid)
                                                .where(bid.goods.goodsId.eq(goods.goodsId)),
                                        "currentBidCount"
                                ),

                                // 좋아요 여부 (해당 사용자가 좋아요 눌렀는지)
                                new CaseBuilder()
                                        .when(like.likeId.isNotNull()).then(true)
                                        .otherwise(false)
                                        .as("isLiked")
                        )
                )
                .from(goods)
                // 좋아요 테이블과 left join (로그인한 사용자 기준)
                .leftJoin(like).on(
                        like.goods.goodsId.eq(goods.goodsId)
                                .and(like.user.userId.eq(userId))
                )
                .where(builder)              // 동적 필터링 조건 적용
                .orderBy(orderSpecifier)     // 정렬
                .offset(pageable.getOffset())// 페이징 offset
                .limit(pageable.getPageSize()) // 페이징 limit
                .fetch(); // 결과 조회

        // 전체 상품 개수 조회 (count 쿼리)
        Long total = queryFactory
                .select(goods.count())
                .from(goods)
                .where(builder)
                .fetchOne();

        // Spring Data Page 객체로 변환
        return PageableExecutionUtils.getPage(content, pageable, () -> total);
    }

    /**
     * 정렬 조건을 문자열로 받아 QueryDSL의 OrderSpecifier로 변환
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
            return goods.createdAt.desc();
        }
        // 기본 정렬 (최신순)
        return goods.createdAt.desc();
    }

    /**
     * 현재 입찰가 계산:
     * - 입찰이 있을 경우: 최고 입찰가
     * - 입찰이 없을 경우: 시작가(minimumBidAmount)
     */
    private NumberExpression<Long> currentBidPriceExpr(QGoodsEntity goods, QBidHistoryEntity bid) {
        // max 입찰가 (null이면 0으로 처리), NumberExpression으로 변환
        NumberExpression<Long> maxBidPrice = Expressions.numberTemplate(Long.class,
                "(select coalesce(max(b.bidPrice), 0) from BidHistoryEntity b where b.goods.goodsId = {0})",
                goods.goodsId
        );

        // maxBidPrice vs minimumBidAmount 비교
        return new CaseBuilder()
                .when(maxBidPrice.gt(goods.minimumBidAmount)).then(maxBidPrice)
                .otherwise(goods.minimumBidAmount);
    }


}
