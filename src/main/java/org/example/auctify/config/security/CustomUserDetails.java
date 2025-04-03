package org.example.auctify.config.security;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.example.auctify.dto.user.Role;
import org.example.auctify.entity.user.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;


/**
 * CustomUserDetails 클래스는 Spring Security의 UserDetails 인터페이스를 구현하여
 * OAuth2 인증 후 받아온 사용자의 정보를 Spring Security에서 관리 가능한 형태로 변환해 주는 역할을 합니다.
 * JWT 토큰 생성 및 관리 시 사용자의 권한과 인증 정보를 담아 처리할 때 활용됩니다.
 *
 * @worker : 조영흔
 * @work : CustomOAuth2UserService에서 넘어온 사용자의 정보와 권한을 처리하여 JWT 토큰 생성에 활용
 * @date : 2025/03/09
 */

@Setter
@Getter
@ToString
@Log4j2
@Component
@NoArgsConstructor
public class CustomUserDetails implements UserDetails {

    // 서비스에서 관리하는 사용자 엔티티
    private UserEntity user;

    /**
     * OAuth2 로그인 시 외부 공급자(Google, Kakao 등)로부터 받은 사용자 속성 정보
     * (예: 이메일, 이름, 프로필 사진 등)
     */
    private Map<String, Object> attributes;

    /**
     * 일반 로그인 시 사용하는 생성자
     * @param userEntity 서비스 내부에서 관리되는 사용자 엔티티
     */
    public CustomUserDetails(UserEntity userEntity) {
        this.user = userEntity;
    }

    /**
     * OAuth2 로그인 시 사용하는 생성자
     * @param userEntity 서비스 내부에서 관리되는 사용자 엔티티
     * @param attributes OAuth2 공급자로부터 받은 사용자 속성 정보
     */
    public CustomUserDetails(UserEntity userEntity, Map<String, Object> attributes) {
        this.user = userEntity;
        this.attributes = attributes;
    }


    /**
     * 사용자가 가진 권한 목록을 반환하는 메소드
     * Spring Security가 인증 및 인가 시 사용자의 권한을 확인하는 데 사용됨.
     * @return 사용자의 권한 목록
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> collection = new ArrayList<>();
        Role userRole = user.getRole();
        System.out.println("뭘까요 에러가 ???" + userRole.toString());

        collection.add(new SimpleGrantedAuthority("ROLE_" + user.getRole().toString()));
        return collection;
    }


    /**
     * OAuth2 로그인이기 때문에 별도의 비밀번호가 없음.
     * 따라서 빈 문자열을 반환함.
     * @return 비어 있는 문자열
     */
    @Override
    public String getPassword() {
        return "";
    }

    /**
     * 사용자의 고유 식별자 (OAuth2 공급자의 식별자)를 반환함.
     * @return OAuth2 공급자로부터 받은 사용자 식별자
     */
    @Override
    public String getUsername() {
        return user.getOauthId();
    }

    /**
     * 사용자 엔티티의 고유 ID를 반환함.
     * 서비스 내에서 사용자를 식별하는 데 사용됨.
     * @return 내부 관리되는 사용자 ID
     */
    public Long getUserId() {
        return user.getUserId();
    }

    /**
     * 아래의 계정 활성화 관련 메소드들은 생략되어 있으며,
     * 필요에 따라 사용자 계정 활성화 여부에 맞게 구현하여 사용 가능함.
     * (기본적으로 true를 리턴하면 계정 활성 상태로 간주)
     */

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
