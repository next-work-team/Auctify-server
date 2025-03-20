package org.example.auctify.dto.duplication;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DuplicationDTO {

    @Schema(description = "결과 응답 메세지")
    private String message;


    @Schema(description = "중복이 아니면 : true   중복이면 : false, 문자에 공백이 포함되면 : false")
    private Boolean isNotDuplication;
}
