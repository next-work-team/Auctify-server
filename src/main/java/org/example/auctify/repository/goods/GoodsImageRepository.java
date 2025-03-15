package org.example.auctify.repository.goods;

import org.example.auctify.entity.Goods.GoodsImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GoodsImageRepository extends JpaRepository<GoodsImageEntity, Long> {
}
