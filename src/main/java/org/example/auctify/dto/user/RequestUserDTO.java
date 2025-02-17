package org.example.auctify.dto.user;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class RequestUserDTO {

    @Schema(description = "닉네임")
    @NotNull(message = "이름은 필수입니다.")
    @Pattern(regexp = "^[a-zA-Z가-힣0-9]{2,20}$", message = "닉네임은 2~20자의 영어, 한글, 숫자만 가능합니다.")
    private String nickName;

    @Schema(description = "생년월일 (YYYY-MM-DD 형식)")
    @NotNull(message = "생년월일은 필수입니다.")
    @DateTimeFormat(pattern = "yyyy-MM-dd") // 요청 데이터를 변환
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd") // JSON 변환
    private LocalDate birthDay;

    @Schema(description = "회원 주소")
    @NotNull(message = "주소는 필수입니다.")
    private AddressDTO address;

    @Schema(description = "이미지 주소")
    private String image;

    @Schema(description = "이미지 주소")
    private String introduction;

}
