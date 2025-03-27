package org.example.auctify.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.auctify.dto.social.CustomOauth2User;
import org.example.auctify.dto.social.UserDTO;
import org.example.auctify.dto.user.Role;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    public JWTFilter(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authorization = null;
        Cookie[] cookies = request.getCookies();
        if(cookies != null && cookies.length >= 1){
            for (Cookie cookie : cookies) {

                if (cookie.getName().equals("Authorization")) {
                    logger.info(cookie.getName());
                    logger.info(cookie.getValue());
                    authorization = cookie.getValue();
                }
            }
        }

        //Authorization 헤더 검증
        if (authorization == null) {
            System.out.println("authorization token이 없습니다.");
            filterChain.doFilter(request, response);
            // 조건이 해당되면 메서드 종료(필수)
            return;
        } else{
            System.out.println("authorization token이 있습니다.");

        }

        //토큰
        String token = authorization;

        if (jwtUtil.isExpired(token)) {
            System.out.println("token expired");
            filterChain.doFilter(request, response);
            // 조건이 해당되면 메소드 종료(필수)
            return;
        }else{
            System.out.println("token이 유효합니다");
        }

        //토근에서 oauthId, nickname, role 획득
        Long userId = jwtUtil.getUserId(token);
        String oauthId = jwtUtil.getOauthId(token);
        String nickname = jwtUtil.getNickname(token);
        String role = jwtUtil.getRole(token);

        //userDTO를 생성하며 값 set
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(userId);
        userDTO.setOauthId(oauthId);
        userDTO.setName(nickname);
        userDTO.setRole(Role.valueOf(role));

        //UserDetails 회원 정보 객체 담기
        CustomOauth2User customOAuth2User = new CustomOauth2User(userDTO);

        //스프링 시큐리티 인증 토큰 생성
        Authentication authToken = new UsernamePasswordAuthenticationToken(customOAuth2User, null, customOAuth2User.getAuthorities());

        // SecurityContext 설정
//        SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
//        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
//        securityContext.setAuthentication(authToken);
//        SecurityContextHolder.setContext(securityContext);

        SecurityContextHolder.getContext().setAuthentication(authToken);
        logger.info("인증객체 권한 확인 에러로그: " + authToken.getAuthorities());


        logger.info("SecurityContext에 인증 정보 설정 완료: " + SecurityContextHolder.getContext().getAuthentication());

        filterChain.doFilter(request, response);
    }

}
