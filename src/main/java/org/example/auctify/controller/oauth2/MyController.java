package org.example.auctify.controller.oauth2;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.example.auctify.dto.response.ApiResponseDTO;
import org.example.auctify.dto.social.CustomOauth2User;
import org.example.auctify.dto.social.UserDTO;
import org.example.auctify.dto.user.UserInfoResponseDTO;
import org.example.auctify.service.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

// 테스트용 회원가입한 사용자만 허용
@RestController
@RequiredArgsConstructor
@Slf4j
public class MyController {
    private final UserService userService;

    @GetMapping("/my")
    @ResponseBody
    public ResponseEntity<ApiResponseDTO<UserInfoResponseDTO>> myAPI(@AuthenticationPrincipal CustomOauth2User userDetails) {


//        null
//        2025-03-14T19:49:50.886+09:00  INFO 90682 --- [io-8080-exec-10] o.e.a.controller.oauth2.MyController     : [LOG]  : null
//        kakao 3937100061
//        2025-03-14T19:49:50.887+09:00  INFO 90682 --- [io-8080-exec-10] o.e.a.controller.oauth2.MyController     : [LOG]  : kakao 3937100061
//        null
//        2025-03-14T19:49:50.888+09:00  INFO 90682 --- [io-8080-exec-10] o.e.a.controller.oauth2.MyController     : [LOG]  : [org.example.auctify.dto.social.CustomOauth2User$1@1f505650]
//        [org.example.auctify.dto.social.CustomOauth2User$1@a2983f6]
//        2025-03-14T19:49:50.888+09:00  INFO 90682 --- [io-8080-exec-10] o.e.a.controller.oauth2.MyController     : [LOG]  : [org.example.auctify.dto.social.CustomOauth2User$1@6c4a0353]
//        ROLE_USER
//        2025-03-14T19:49:50.888+09:00  INFO 90682 --- [io-8080-exec-10] o.e.a.controller.oauth2.MyController     : [LOG]  : ROLE_USER
        try {

        log.info("[LOG] userDetails.getUserId  : "+userDetails.getUserId());
        System.out.println(userDetails.getUserId());

        log.info("[LOG] userDetails.getName : "+userDetails.getName());
        System.out.println(userDetails.getName());

        log.info("[LOG] userDetails.getOauthId(  : "+userDetails.getOauthId());
        System.out.println(userDetails.getOauthId());

        log.info("[LOG] userDetails.getAttributes  : "+userDetails.getAttributes());
        System.out.println(userDetails.getAttributes());

        log.info("[LOG] userDetails.getAuthorities : "+userDetails.getAuthorities());
        System.out.println(userDetails.getAuthorities());

        log.info("[LOG] userDetails.getRole : "+userDetails.getRole());
        System.out.println(userDetails.getRole());

            log.error("[LOG] 마이프로필: {}");

            UserInfoResponseDTO userProfile = userService.getProfile(userDetails.getUserId());
            return ResponseEntity.ok(ApiResponseDTO.success(userProfile));
        } catch (Exception e) {
            log.error("[LOG] Internal server error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponseDTO.error(400, "Get My Profile"));
        }    }

}
