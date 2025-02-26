package org.example.auctify.entity.user;

import jakarta.persistence.*;
import lombok.*;
import org.example.auctify.dto.user.Role;

import java.lang.annotation.ElementType;
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

    @Column(name = "oauth_id", nullable = false )
    private String oauthId;


    @Column(name = "nickName", nullable = true)
    private String nickName;

    @Column(name = "email", nullable = true)
    private String email;

    @Column(name = "birthday", nullable = true)
    private LocalDate birthday;

    @Embedded
    private AddressEntity address;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @Column(name = "image", nullable = true)
    private String image;

    @Column( name = "introduction", nullable = true)
    private String introduction;


}
