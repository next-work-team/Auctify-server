package org.example.auctify.dto.Goods;

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
public class GoodsRequestDTO {

    @NotBlank(message = "상품 이름은 필수입니다.")
    @Size(min = 1, max = 10, message = "상품 이름은 1~10자 이내여야 합니다.")
    private String goodsName;

    @NotBlank(message = "상품 설명은 필수입니다.")
    @Size(min = 20, max = 2000, message = "상품 설명은 20~2000자 이내여야 합니다.")
    private String goodsDescription;

    @NotBlank(message = "카테고리 선택은 필수입니다.(id 값입니다. 예상 1=전자기기, 2=패션, 3=예술품, 4=수집품)")
    private Long categoryId;

    @NotNull(message = "최소 입찰 금액은 필수입니다.")
    @Min(value = 1, message = "최소 입찰 금액은 1원 이상이어야 합니다.")
    private Long minimumBidAmount;

    @NotBlank(message = "상품 상태는 필수입니다.")
    private String goodsStatus;

    @Future(message = "경매 종료 시간을 현재 시각보다 이후로 설정해야 합니다.")
    @NotNull(message = "경매 종료 시간은 필수입니다.")
    private LocalDateTime actionEndTime;

    @PositiveOrZero(message = "즉시 구매가는 0원 이상이어야 합니다. (옵션입니다.)")
    private Long buyNowPrice;

    @Size(max = 10, message = "최대 10장의 이미지만 업로드할 수 있습니다.")
    private List<MultipartFile> image;
    //Service 에서 File Image Entity 로 전환요청할것
}
