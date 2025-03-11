package org.example.auctify.dto.user;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.example.auctify.entity.user.AddressEntity;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class AddressDTO {
    @Schema(description = "주소")
    private String userAddress;
    @Schema(description = "상세 주소")
    private String userAddressDetail;
    @Schema(description = "우편 번호")
    private String userZipCode;

    public static AddressDTO changeDTO(AddressEntity address) {
        return AddressDTO.builder()
                .userAddress(address.getUserAddr())
                .userAddressDetail(address.getUserAddrDetail())
                .userZipCode(address.getUserZipCode())
                .build();
    }

    public static AddressDTO addAddress(String userAddr, String userAddressDetail, String userZipCode) {
        return AddressDTO.builder()
                .userAddress(userAddr)
                .userAddressDetail(userAddressDetail)
                .userZipCode(userZipCode)
                .build();
    }

}
