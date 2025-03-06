package org.example.auctify.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsMvcConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry corsRegistry) {
        corsRegistry.addMapping("/**")
                .allowedOrigins("https://localhost:3000", "https://www.auctify.shop") // 정확한 도메인 지정
                .allowedMethods("*") // 모든 HTTP 메서드 허용
                .allowedHeaders("*") // 모든 헤더 허용
                .allowCredentials(true) // ✅ 쿠키 전송 허용 (allowedOrigins("*")와 함께 사용 불가)
                .exposedHeaders("Authorization", "Set-Cookie"); // ✅ 클라이언트가 Authorization 헤더 받을 수 있도록 허용
    }
}
