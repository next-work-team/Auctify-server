package org.example.auctify.dto.user;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserInfoResponseDTO {

    @Schema(description = "회원 닉네임")
    private String nickName;

    @Schema(description = "회원 이미지")
    private String profileImage;

    @Schema(description = "회원 생년월일")
    private String birthdate;

    @Schema(description = "평균 온도 ")
    private double mannerTemperature;

    @Schema
    private AddressDTO addressDTO;

}
