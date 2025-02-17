package org.example.auctify.entity.user;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

/**
 * worker : 조영흔
 * work : 유저에 관한 테이블을 생성해주는 엔티티 클래스
 * date    : 2025/02/17
 */

@Entity(name = "user")
@Getter
@ToString()
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class UserEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;


    @Column(name = "nickName", nullable = false)
    private String nickName;


    @Column(name = "birthday")
    private LocalDate birthday;

    @Embedded
    private AddressEntity address;


    @Column(name = "image")
    private String image;

    @Column( name = "introduction")
    private String introduction;


}
