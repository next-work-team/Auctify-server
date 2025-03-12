package org.example.auctify.dto.Goods;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.*;

/**
 * worker : 조영흔
 * work : 좋아요 요청 DTO {좋아요(찜)하는 건지 취소하는건지 정보 필수}
 * date    : 2025/03/12
 */
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonFormat(shape = JsonFormat.Shape.STRING)
public class LikeRequestDTO {

    @Schema(description = "경매 id")
    private Long goodsId;

    @Schema(description = "좋아요 여부")
    @Pattern(regexp = "like|unLiked", message = "like 또는 unLiked만 허용됩니다.")
    private String like;
}
