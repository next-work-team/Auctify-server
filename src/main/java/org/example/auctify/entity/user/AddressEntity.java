package org.example.auctify.entity.user;


import jakarta.persistence.*;
import lombok.*;
import org.example.auctify.dto.user.AddressDTO;
import org.example.auctify.entity.BaseTimeEntity;


/**
 * worker : 조영흔
 * work : 유저의 주소 관한 테이블을 생성해주는 엔티티 클래스
 * date    : 2025/02/17
 */

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity(name= "address")
public class AddressEntity   extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Long addressId;

    @Column(name="addr", nullable = false)
    private String addr;

    @Column(name="addrDetail", nullable = false)
    private String addrDetail;

    @Column(name="zipCode", nullable = false)
    private String zipCode;

    @Column(name="defaultAddress", nullable = true)
    private String defaultAddress;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;


    public static AddressEntity changeEntity(AddressDTO addressDTO) {
        return AddressEntity.builder()
                .addr(addressDTO.getAddr())
                .defaultAddress(addressDTO.getDefaultAddress())
                .addrDetail(addressDTO.getAddrDetail())
                .zipCode(addressDTO.getZipCode())
                .build();
    }



}
