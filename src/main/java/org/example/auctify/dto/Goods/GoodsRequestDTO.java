package org.example.auctify.dto.Goods;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.STRING)

public class GoodsRequestDTO {
    @NotNull(message = "등록하는 유저id 는 필수입니다.")
    private Long userId;

    @NotBlank(message = "상품 이름은 필수입니다.")
    @Size(min = 1, max = 10, message = "상품 이름은 1~10자 이내여야 합니다.")
    private String goodsName;

    @NotBlank(message = "상품 설명은 필수입니다.")
    @Size(min = 20, max = 2000, message = "상품 설명은 20~2000자 이내여야 합니다.")
    private String goodsDescription;

    @NotNull(message = "    ELECTRONICS,       = 전자기기 \n" +
            "    FASHION,           = 패션\n" +
            "    COLLECTIBLES,      = 수집품\n" +
            "    ARTWORK,           = 예술작품 \n" +
            "    VEHICLES,          = 자동차 \n" +
            "    JEWELRY_WATCHES,   = 보석/시계 \n" +
            "    SPORTS,            = 스포츠 \n" +
            "    FURNITURE_DECOR,   = 가구/인테리어 \n" +
            "    BOOKS,             = 도서 \n" +
            "    MUSIC_INSTRUMENTS, = 음악/악기 \n" +
            "    ANTIQUES,          = 골동품 \n" +
            "    ETC = 기타   카테고리 선택은 필수입니다.")
    private GoodsCategory category;

    @NotNull(message = "최소 입찰 금액은 필수입니다.")
    @Min(value = 1, message = "최소 입찰 금액은 1원 이상이어야 합니다.")
    private Long minimumBidAmount;

    @NotBlank(message = "NEW = 새것 \n USED = 입찰중  상품 상태는 필수입니다.")
    private String goodsStatus;

    @NotBlank(message = "AWARDED = 낙찰 \n BIDDING = 입찰중  상품 상태는 필수입니다.")
    private String goodsProcessStatus;

    @Future(message = "경매 종료 시간을 현재 시각보다 이후로 설정해야 합니다.")
    @NotNull(message = "경매 종료 시간은 필수입니다.")
    private LocalDateTime actionEndTime;

    @PositiveOrZero(message = "즉시 구매가는 0원 이상이어야 합니다. (옵션입니다.)")
    private Long buyNowPrice;

    @Size(max = 10, message = "최대 10장의 이미지만 업로드할 수 있습니다.")
    private List<String> image;

    //Service 에서 File Image Entity 로 전환요청할것
}
