package org.example.auctify.repository.goods;

import org.example.auctify.entity.Goods.GoodsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GoodsRepository extends JpaRepository<GoodsEntity, Long> {
}
