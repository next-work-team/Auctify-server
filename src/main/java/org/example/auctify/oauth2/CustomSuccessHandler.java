package org.example.auctify.oauth2;


import com.nimbusds.jwt.JWT;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.auctify.dto.social.CustomOauth2User;
import org.example.auctify.jwt.JWTUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.CookieRequestCache;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

@Component
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JWTUtil jwtUtil;
    private final RequestCache requestCache = new HttpSessionRequestCache();

    public CustomSuccessHandler(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
        System.out.println("CustomSuccessHandler가 정상적으로 로드됨!!!");
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        System.out.println("인증에 성공했습니다 인증인증");
        //OAuth2User
        CustomOauth2User customUserDetails = (CustomOauth2User) authentication.getPrincipal();

        String oauthId = customUserDetails.getOauthId();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> interator = authorities.iterator();
        GrantedAuthority auth = interator.next();
        String role = auth.getAuthority();

        String token = jwtUtil.createJwt(oauthId, role, 60 * 60 * 60 * 60L);

        response.addCookie(createCookie("Authorization", token));
        // 사용자가 원래 요청했던 URL 가져오기
        SavedRequest savedRequest = requestCache.getRequest(request, response);
        String targetUrl = (savedRequest != null) ? savedRequest.getRedirectUrl() : "http://localhost:3000/";

        System.out.println("리디렉션할 URL: " + targetUrl);


        response.sendRedirect(targetUrl);

    }

    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(60 * 60 * 60 * 60);

        // Secure=false → 쿠키가 HTTP와 HTTPS 둘 다 전송됨 클라가 Https일때는 true
        cookie.setSecure(false); // HTTPS가 아닐 때도 테스트할 수 있도록 설정

        cookie.setPath("/"); // 도메인의 모든 페이지 경로에 쿠키를 전송하도록 지정
        cookie.setHttpOnly(true);

        // CORS 문제 해결 (리디렉션 후에도 쿠키 유지)
        cookie.setAttribute("SameSite", "None");

        return cookie;
    }

}
