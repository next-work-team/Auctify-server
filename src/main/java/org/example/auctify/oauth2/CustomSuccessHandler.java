package org.example.auctify.oauth2;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.auctify.dto.social.CustomOauth2User;
import org.example.auctify.jwt.JWTUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

/**
 * CustomSuccessHandler 클래스는 OAuth2 로그인 인증 성공 후 실행되는 핸들러입니다.
 * 인증 성공 시 사용자 정보를 이용하여 JWT 토큰을 생성하고 쿠키로 클라이언트에게 전달합니다.
 * 이후 설정된 URL로 리디렉션하여 사용자 경험을 개선합니다.
 */
@Component
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JWTUtil jwtUtil;

    // 원본 요청 URL을 유지하기 위한 캐시 (필요 시 활용 가능)
    private final RequestCache requestCache = new HttpSessionRequestCache();

    public CustomSuccessHandler(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
        System.out.println("CustomSuccessHandler가 정상적으로 로드됨!!!");
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        System.out.println("인증에 성공했습니다 인증인증");

        // 인증된 사용자 정보 추출
        CustomOauth2User customUserDetails = (CustomOauth2User) authentication.getPrincipal();
        String oauthId = customUserDetails.getOauthId();
        Long userId = customUserDetails.getUserId();
        String name = customUserDetails.getName();

        // 사용자의 권한 정보 추출
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        // JWT 토큰 생성 (24일간 유효)
        String token = jwtUtil.createJwt(userId, name, oauthId, role, 60 * 60 * 1000L * 24 * 24);

        // 쿠키 생성 및 설정 (JWT 포함)
        response.addCookie(createCookie("Authorization", token, request));

        // 원래 요청된 URL 가져오기 (필요 시 활용)
        SavedRequest savedRequest = requestCache.getRequest(request, response);

        // 리디렉션할 URL 결정 (로컬 및 배포 환경에 따라 다름)
        String serverName = request.getServerName();
        String targetUrl;

        if (serverName.contains("localhost")) {
            targetUrl = "http://localhost:8080/my";
        } else {
            targetUrl = "https://www.auctify.shop";
        }

        System.out.println("리디렉션할 URL: " + targetUrl);
        response.setHeader("Access-Control-Expose-Headers", "Set-Cookie");

        // 설정된 URL로 리디렉션 수행
        getRedirectStrategy().sendRedirect(request, response, targetUrl);

        // 필요 시 JSON 응답 형태로 변경 가능 (주석 처리된 예시 참고)
        // response.setContentType("application/json");
        // response.setCharacterEncoding("UTF-8");
        // response.getWriter().write("{\"message\": \"success\", \"token\": \"" + token + "\"}");
    }

    /**
     * 쿠키를 생성하는 유틸리티 메소드
     * @param key 쿠키의 이름 (여기서는 "Authorization")
     * @param value 쿠키의 값 (JWT 토큰)
     * @param request HTTP 요청 객체
     * @return 설정된 쿠키 객체
     */
    private Cookie createCookie(String key, String value, HttpServletRequest request) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(3600 * 24 * 24);  // 24일 유효

        boolean isLocal = request.getServerName().contains("localhost");

        cookie.setSecure(!isLocal);  // 로컬은 Secure 비활성화
        cookie.setHttpOnly(true);    // JavaScript 접근 방지

        if (!isLocal) {
            cookie.setDomain("auctify.shop");
            cookie.setAttribute("SameSite", "None");  // 배포환경은 SameSite=None
        } else {
            cookie.setAttribute("SameSite", "Lax");   // 로컬환경은 SameSite=Lax
        }

        cookie.setPath("/");  // 모든 경로에 쿠키 적용

        return cookie;
    }
}
