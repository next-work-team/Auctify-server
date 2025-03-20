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
import org.springframework.security.core.context.SecurityContextHolder;
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
        Long userId = customUserDetails.getUserId();
        String name = customUserDetails.getName();



        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> interator = authorities.iterator();
        GrantedAuthority auth = interator.next();
        String role = auth.getAuthority();



        // 1시간 = 3600 000 /  24일로 설정
        String token = jwtUtil.createJwt(userId,name,oauthId,role, 60 * 60 * 1000L * 24 * 24);

        response.addCookie(createCookie("Authorization", token,request));
        // 사용자가 원래 요청했던 URL 가져오기
        SavedRequest savedRequest = requestCache.getRequest(request, response);



        String serverName = request.getServerName();
        String targetUrl;

        if (serverName.contains("localhost")) {
            // React가 없으면 일단 임시로 백엔드 API 경로 또는 성공페이지
            targetUrl = "http://localhost:8080/my";
        } else {
            targetUrl = "https://www.auctify.shop";
        }

        System.out.println("리디렉션할 URL: " + targetUrl);
        response.setHeader("Access-Control-Expose-Headers", "Set-Coookie");


        getRedirectStrategy().sendRedirect(request, response, targetUrl);



        // ✅ 리디렉션 대신 JSON 응답 반환
//        response.setContentType("application/json");
//        response.setCharacterEncoding("UTF-8");
//        response.getWriter().write("{\"message\": \"success\", \"token\": \"" + token + "\"}");

    }

    private Cookie createCookie(String key, String value, HttpServletRequest request) {

        //1시간 = 3600 / 24일로 설정
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(3600 * 24 * 24);

        boolean isLocal = request.getServerName().contains("localhost");

        cookie.setSecure(!isLocal);  // 로컬 환경에서는 Secure = false
        cookie.setHttpOnly(true);

        if (!isLocal) {
            cookie.setDomain("auctify.shop");  // 로컬이 아니면 설정
            cookie.setAttribute("SameSite", "None");
        } else {
            cookie.setAttribute("SameSite", "Lax");  // 로컬에서는 Lax 또는 Strict로 설정
        }

        cookie.setPath("/");


        return cookie;
    }

}
