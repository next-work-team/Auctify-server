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

    @Schema(description = "주소 번호")
    private Long addressId;

    @Schema(description = "주소")
    private String addr;
    @Schema(description = "상세 주소")
    private String addrDetail;
    @Schema(description = "우편 번호")
    private String zipCode;
    @Schema(description = "기본주소여부 ")
    private Boolean defaultAddress;

    public static AddressDTO changeDTO(AddressEntity address) {
        return AddressDTO.builder()
                .addressId(address.getAddressId())
                .addr(address.getAddr())
                .addrDetail(address.getAddrDetail())
                .zipCode(address.getZipCode())
                .defaultAddress(address.getDefaultAddress())
                .build();
    }

    public static AddressDTO addAddress(String userAddr, String userAddressDetail, String userZipCode,boolean defaultAddress) {
        return AddressDTO.builder()
                .addr(userAddr)
                .addrDetail(userAddressDetail)
                .zipCode(userZipCode)
                .defaultAddress(defaultAddress)
                .build();
    }


}
