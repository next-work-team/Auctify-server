package org.example.auctify.repository.goods;

import org.example.auctify.dto.Goods.GoodsResponseSummaryDTO;
import org.example.auctify.entity.Goods.GoodsEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GoodsRepositoryCustom {

    Page<GoodsEntity> searchGoods(
            String category,
            Double priceRangeLow,
            Double priceRangeHigh,
            String goodsStatus,
            String goodsProcessStatus,
            String goodsName,
            String sort,
            Pageable pageable);
}
