package org.example.auctify.entity.user;


import jakarta.persistence.Embeddable;
import lombok.*;
import org.example.auctify.dto.user.AddressDTO;


/**
 * worker : 조영흔
 * work : 유저의 주소 관한 테이블을 생성해주는 엔티티 클래스
 * date    : 2025/02/17
 */

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Embeddable
public class AddressEntity {
    private String userAddr;
    private String userAddrDetail;
    private String userZipCode;


    public static AddressEntity changeEntity(AddressDTO addressDTO) {
        return AddressEntity.builder()
                .userAddr(addressDTO.getUserAddress())
                .userAddrDetail(addressDTO.getUserAddressDetail())
                .userZipCode(addressDTO.getUserZipCode())
                .build();
    }



}
