package org.example.auctify.dto.social;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class CustomOauth2User implements OAuth2User {

    private final UserDTO userDTO;

    public CustomOauth2User(UserDTO userDTO) {
        this.userDTO = userDTO;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> collection = new ArrayList<>();

        collection.add(new GrantedAuthority() {

            @Override
            public String getAuthority() {

                return userDTO.getRole().toString();
            }
        });

        return collection;
    }
    @Override
    public String getName() {

        return userDTO.getName();
    }

    public String getOauthId() {

        return userDTO.getOauthId();
    }

    public String getRole() {

        return userDTO.getRole().toString();
    }



}
