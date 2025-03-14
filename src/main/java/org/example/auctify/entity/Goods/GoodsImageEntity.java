package org.example.auctify.entity.Goods;

import jakarta.persistence.*;
import lombok.*;
import org.example.auctify.entity.BaseTimeEntity;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity(name = "goods_image")
public class GoodsImageEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long imageId;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="goods_id", nullable = false)
    private GoodsEntity goodsId;

    private String image_src;

    public static GoodsImageEntity of(GoodsEntity goods, String image_src){
        return GoodsImageEntity.builder()
                .goodsId(goods)
                .image_src(image_src)
                .build();
    }
}
