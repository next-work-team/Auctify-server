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
    private String addr;
    @Schema(description = "상세 주소")
    private String addrDetail;
    @Schema(description = "우편 번호")
    private String zipCode;
    @Schema(description = "기본주소여부 ")
    private String defaultAddress;

    public static AddressDTO changeDTO(AddressEntity address) {
        return AddressDTO.builder()
                .addr(address.getAddr())
                .addrDetail(address.getAddrDetail())
                .zipCode(address.getZipCode())
                .defaultAddress(address.getDefaultAddress())
                .build();
    }

    public static AddressDTO addAddress(String userAddr, String userAddressDetail, String userZipCode,String defaultAddress) {
        return AddressDTO.builder()
                .addr(userAddr)
                .addrDetail(userAddressDetail)
                .zipCode(userZipCode)
                .defaultAddress(defaultAddress)
                .build();
    }

}
