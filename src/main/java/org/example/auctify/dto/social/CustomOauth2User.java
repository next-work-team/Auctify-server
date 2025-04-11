package org.example.auctify.dto.social;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collections;
import java.util.Collection;
import java.util.Map;

public class CustomOauth2User implements OAuth2User {

    private final UserDTO userDTO;

    public CustomOauth2User(UserDTO userDTO) {
        System.out.println("userDTO 가져온거 " + userDTO.toString());
        this.userDTO = userDTO;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return Collections.emptyMap(); // 빈 Map 리턴이 안전함
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        System.out.println("뭘까요 에러가 ???  CustomOauth2User.getAuthorities() :" + userDTO.getRole().name());

        return Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_"+userDTO.getRole().name())
        );
    }

    @Override
    public String getName() {
        return userDTO.getName();
    }

    public String getOauthId() {
        return userDTO.getOauthId();
    }

    public String getRole() {
        return userDTO.getRole().name();
    }

    public Long getUserId() {
        return userDTO.getUserId();
    }
}
