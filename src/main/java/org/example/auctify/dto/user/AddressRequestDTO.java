package org.example.auctify.dto.user;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class AddressRequestDTO {

    @Schema(description = "주소", nullable = false)
    private String addr;
    @Schema(description = "상세 주소")
    private String addrDetail;
    @Schema(description = "우편 번호")
    private String zipCode;

}
