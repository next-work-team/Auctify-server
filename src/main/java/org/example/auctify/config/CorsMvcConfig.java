package org.example.auctify.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsMvcConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry corsRegistry) {
        corsRegistry.addMapping("/**")
                .exposedHeaders("set-Cookie")
                .allowedOrigins("*") // 모든 출처 허용 (단, credentials 사용 불가)
                .allowedMethods("*") // 모든 HTTP 메소드 허용
                .allowedHeaders("*"); // 모든 헤더 허용
    }
}
