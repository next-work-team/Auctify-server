package org.example.auctify.config.security;

import jakarta.servlet.http.HttpServletRequest;
import org.example.auctify.jwt.JWTFilter;
import org.example.auctify.jwt.JWTUtil;
import org.example.auctify.oauth2.CustomSuccessHandler;
import org.example.auctify.service.user.CustomOAuth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationCodeGrantFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * SecurityConfig 클래스는 Spring Security를 설정하고,
 * JWT 기반 인증, OAuth2 로그인, CORS 정책 등을 구성합니다.
 */
@Configuration
@EnableWebSecurity  // Spring Security 활성화 및 구성
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomSuccessHandler customSuccessHandler;
    private final JWTUtil jwtUtil;

    @Autowired
    public SecurityConfig(CustomOAuth2UserService customOAuth2UserService, CustomSuccessHandler customSuccessHandler, JWTUtil jwtUtil) {
        this.customOAuth2UserService = customOAuth2UserService;
        this.customSuccessHandler = customSuccessHandler;
        this.jwtUtil = jwtUtil;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // CORS 설정
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()));

        // CSRF 보호 비활성화 (JWT 기반이므로 필요 없음)
        http.csrf(auth -> auth.disable());

        // 폼 로그인 방식 비활성화
        http.formLogin(auth -> auth.disable());

        // HTTP 기본 인증 비활성화
        http.httpBasic(auth -> auth.disable());

        // 세션을 사용하지 않는 상태로 설정 (JWT 기반 인증)
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // JWT 필터 추가 (OAuth2 필터 앞에 JWT 필터가 동작하도록 설정)
        http.addFilterBefore(new JWTFilter(jwtUtil), OAuth2AuthorizationCodeGrantFilter.class);

        // OAuth2 로그인 설정
        http.oauth2Login(oauth2 -> oauth2
                .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService))
                .successHandler(customSuccessHandler));

        // URL별 접근 권한 설정
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/login", "/oauth2/**", "/login/oauth2/**", "/favicon.ico", "/presigned", "123",
                        "/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**", "/auth/**","/api/pay/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/user/{userId}").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/auction/{goodsId}").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/auction/search").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/auction/bidSummary").permitAll()
                .requestMatchers("/api/**").hasRole("USER")
                //.requestMatchers(HttpMethod.GET, "/api/user").hasRole("USER")


                .requestMatchers("/my").hasRole("USER")  // '/my' 경로는 USER 권한 필요
                .anyRequest().authenticated());  // 그 외 모든 요청은 인증 필요

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        // 허용 가능한 기본 도메인 리스트
        List<String> whitelist = List.of(
                "https://localhost:3000",
                "https://www.auctify.shop"
        );

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", new CorsConfiguration() {
            @Override
            public CorsConfiguration clone() throws CloneNotSupportedException {
                // 매 요청마다 새로운 객체가 필요하므로 clone() 메서드만 오버라이드
                return (CorsConfiguration) super.clone();
            }
        }.applyPermitDefaultValues()); // applyPermitDefaultValues() 로 기본 메서드·헤더 설정을 깔끔하게 가져옵니다.

        // 실제 configurationSource 로직
        return request -> {
            CorsConfiguration config = new CorsConfiguration();

            String origin = request.getHeader(HttpHeaders.ORIGIN);
            if (origin != null && whitelist.contains(origin)) {
                // 요청한 Origin 값만 동적으로 허용
                config.setAllowedOrigins(Collections.singletonList(origin));
            }

            config.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS"));
            config.setAllowedHeaders(List.of("*"));
            config.setAllowCredentials(true);
            config.setExposedHeaders(List.of("Authorization", "Set-Cookie"));
            config.setMaxAge(3600L);

            return config;
        };
    }

    /**
     * AuthenticationManager 빈 등록
     * 인증 프로세스를 관리하며 인증 요청을 처리합니다.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
