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


/*
 *   worker : 조영흔
 *   work : CustomOAuth2UserService 넘어온
 *          유저의 정보와 권한을 받아와서 JWT를 만드는데 도움을 준다.
 *   date : 2025/03/09
 * */

@Setter
@Getter
@ToString
@Log4j2
@Component
@NoArgsConstructor
public class CustomUserDetails implements UserDetails {



    private UserEntity user;
    // OAuth2 로그인 정보를 저장하기 위한 필드
    // 일반적으로 attributes에는 사용자의 아이디(ID), 이름, 이메일 주소, 프로필 사진 URL 등의 정보가 포함됩니다.
    private Map<String, Object> attributes;


    public CustomUserDetails(UserEntity userEntity) {
        this.user = userEntity;
    }


    public CustomUserDetails(UserEntity userEntity, Map<String, Object> attributes) {
        this.user = userEntity;
        this.attributes = attributes;
    }



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> collection = new ArrayList<>();
        Role userRole = user.getRole();
        collection.add(new SimpleGrantedAuthority(user.getRole().toString()));
        return collection;
    }


    // 전부 소셜 로그인만 존재함.
    @Override
    public String getPassword() {
        return "";
    }

    //식별자 정보를 리턴함.
    @Override
    public String getUsername() {
        return user.getOauthId();
    }

}
