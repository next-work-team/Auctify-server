package org.example.auctify.service.user;

import org.example.auctify.dto.social.*;
import org.example.auctify.dto.user.Role;
import org.example.auctify.entity.user.UserEntity;
import org.example.auctify.repository.user.UserRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Objects;


/*
 *   worker : 조영흔
 *   work : CustomOAuth2UserService에 주요 코드에 주석을 추가함
 *
 *   date : 2025/03/10
 * */
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println("CustomOAuth2UserService  loadUser 실행됨");
        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println(oAuth2User.getAttributes());
        System.out.println("🔥 OAuth2 사용자 정보: " + oAuth2User);

        String registration = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = null;

        if (registration.equals("kakao")) {
            System.out.println("kakao start");

            oAuth2Response = new KakaoResponse(oAuth2User.getAttributes());
        }
        else if (registration.equals("google")) {
            System.out.println("google start");
            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
        }
        else{


            return null;
        }

        // OAuthId가 해당 소셜 로그인 유저의 고유 식별자이다.
        String oauthId = oAuth2Response.getProvider() + " " + oAuth2Response.getProviderId();

        UserEntity existData = userRepository.findByOauthId(oauthId);

        if (existData == null) {

            UserEntity userEntity = UserEntity.builder()
                    .oauthId(oauthId) // 식별하는 값
                    .email(oAuth2Response.getEmail())
                    .nickName(oAuth2Response.getName())
                    .role(Role.ROLE_USER)
                    .build();
            userRepository.save(userEntity);
            UserDTO userDTO = new UserDTO();
            userDTO.setOauthId(oauthId);
            userDTO.setName(oAuth2Response.getName());
            userDTO.setRole(Role.ROLE_USER);
            return new CustomOauth2User(userDTO);

        } else {

            if (Objects.equals(existData.getEmail(), "") || existData.getEmail() == null) {
                String oAuth2Email = oAuth2Response.getName();
                existData.onChangeEmail(oAuth2Email);
            }
            UserDTO userDTO = new UserDTO();
            userDTO.setOauthId(existData.getOauthId());
            userDTO.setName(existData.getNickName());
            userDTO.setRole(Role.ROLE_USER);
            return new CustomOauth2User(userDTO);
        }

    }
}
