package org.example.auctify.config.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.auctify.jwt.JWTFilter;
import org.example.auctify.jwt.JWTUtil;
import org.example.auctify.oauth2.CustomSuccessHandler;
import org.example.auctify.service.user.CustomOAuth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationCodeGrantFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity  // 컴포넌트 스캔을 위한 에너테이션
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


        http
                .cors(corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {

                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {

                        CorsConfiguration configuration = new CorsConfiguration();

                        configuration.setAllowedOrigins(Arrays.asList(
                                "https://localhost:3000",
                                "https://www.auctify.shop"
                        ));
                        configuration.setAllowedMethods(Collections.singletonList("*"));
                        configuration.setAllowCredentials(true); // ✅ 쿠키 전송 허용
                        configuration.setAllowedHeaders(Collections.singletonList("*"));
                        configuration.setExposedHeaders(Arrays.asList("Set-Cookie", "Authorization"));
                        configuration.setMaxAge(3600L); // 1시간 캐시


                        return configuration;
                    }
                }));


        // csrf disable
        http
                .csrf((auth) -> auth.disable()); // CSRF 보호 비활성화 세션 기반일 때 사용 Jwt토큰 기반인증이므로 disable

        // From 로그인 방식 disable
        http
                .formLogin((auth) -> auth.disable());

        // Http Basic 인증 방식 disable
        http
                .httpBasic((auth) -> auth.disable());

        http
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

//        http
//                .addFilterBefore(new JWTFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);
        //JWTFilter 추가
        http
                .addFilterBefore(new JWTFilter(jwtUtil), OAuth2AuthorizationCodeGrantFilter.class);



        // oauth2
        http
                .oauth2Login((oauth2) -> oauth2
                        .userInfoEndpoint((userInfoEndpointConfig) -> userInfoEndpointConfig
                                .userService(customOAuth2UserService))
                        .successHandler(customSuccessHandler));

        // 글로벌 인가 작업
        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/","/login","/oauth2/**", "/login/oauth2/**", "/favicon.ico","/presigned","123","/swagger-ui.html","/swagger-ui/**"
                        , "/v3/api-docs/**", "/swagger-resources/**", "/auth/**").permitAll() // ✅ 명확하게 허용할 것만 작성
                        .requestMatchers(HttpMethod.POST,"/presigned").permitAll()
                        .requestMatchers("/my").hasRole("USER")  // ✅ USER 권한 필요
                        .anyRequest().authenticated());  // ✅ 나머지는 인증 필요

//        http
//                .exceptionHandling(exceptions -> exceptions
//                .authenticationEntryPoint((request, response, authException) -> {
//                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
//                }));

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}
