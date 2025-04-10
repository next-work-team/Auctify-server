package org.example.auctify.config.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

////    @Value("${spring.jwt.secret}")
////    private String secretKeyRaw; // 문자열로 받음
//
//    private final UserDetailsService userDetailsService;
//
//    private final long tokenValidityInMilliseconds = 1000L * 60 * 60 * 24 *24; // 24일
//
//    private Key getSigningKey() {
//        return Keys.hmacShaKeyFor(secretKeyRaw.getBytes(StandardCharsets.UTF_8));
//    }
//
//    // ✅ JWT 토큰 유효성 검증
//    public boolean validateToken(String token) {
//        try {
//            Jwts.parser()
//                    .setSigningKey(getSigningKey())
//                    .build()
//                    .parseClaimsJws(token);
//            return true;
//        } catch (JwtException | IllegalArgumentException e) {
//            return false;
//        }
//    }
//
//    // ✅ 토큰에서 사용자 이름 추출
//    public String getUsername(String token) {
//        return Jwts.parser()
//                .setSigningKey(getSigningKey())
//                .build()
//                .parseClaimsJws(token)
//                .getBody()
//                .getSubject();
//    }
//
//    // ✅ 토큰으로부터 Authentication 객체 생성
//    public Authentication getAuthentication(String token) {
//        String username = getUsername(token);
//        CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(username);
//        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
//    }
//
//    // ✅ JWT 토큰 생성 (OAuth2 인증 후 발급)
//    public String createToken(String username) {
//        Date now = new Date();
//        Date expiry = new Date(now.getTime() + tokenValidityInMilliseconds);
//
//        return Jwts.builder()
//                .setSubject(username)
//                .setIssuedAt(now)
//                .setExpiration(expiry)
//                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
//                .compact();
//    }
}
