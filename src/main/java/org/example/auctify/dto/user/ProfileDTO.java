package org.example.auctify.dto.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalDate;


@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileDTO {


    @Schema(description = "닉네임")
    @NotNull(message = "이름은 필수입니다.")
    @Pattern(regexp = "^[a-zA-Z가-힣0-9]{2,20}$", message = "닉네임은 2~20자의 영어, 한글, 숫자만 가능합니다.")
    private String nickName;


    @Schema(description = "이미지")
    @NotNull(message = "이미지 주소")
    private String imagedURL;



    @Schema(description = "생년월일")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @NotNull
    private LocalDate birthDate;

    //주소
    AddressDTO addressDTO;




}
