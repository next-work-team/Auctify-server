package org.example.auctify.dto.user;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserInfoRequestDTO {

    @Schema(description = "회원 닉네임")
    private String nickname;

    @Schema(description = "회원 이미지")
    private String profileImage;

    //Jackson 을 이용하면 yyyy-mm-dd 형식의 문자열은 자동으로 인식한다.
    @Schema(description = "회원 생년월일")
    private LocalDate birthdate;

    @Schema(description = "바꿀 기본 주소", nullable = false)
    private AddressDTO addressDTO;

}
