package org.example.auctify.repository.goods;

import org.example.auctify.entity.Goods.GoodsCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GoodsCategoryRepository extends JpaRepository<GoodsCategoryEntity, Long> {
}
