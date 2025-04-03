package org.example.auctify.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * CorsMvcConfig 클래스는 Spring MVC의 CORS(Cross-Origin Resource Sharing) 설정을 담당합니다.
 *
 * 이 설정을 통해 클라이언트 애플리케이션(예: React 등)이 서버의 리소스에 접근할 수 있도록 허용된
 * 도메인, 메서드, 헤더 등을 관리합니다. 보통 API 서버에서 프론트엔드 애플리케이션과의 원활한 통신을 위해 사용됩니다.
 */
@Configuration
public class CorsMvcConfig implements WebMvcConfigurer {

    /**
     * CORS 설정 메소드
     * @param corsRegistry CORS 설정을 등록할 레지스트리
     */
    @Override
    public void addCorsMappings(CorsRegistry corsRegistry) {
        corsRegistry.addMapping("/**")  // 모든 URL 패턴에 대해 CORS를 적용
                .allowedOrigins("http://localhost:3000", "https://www.auctify.shop")
                .allowedMethods("*")  // 모든 HTTP 메서드(GET, POST, PUT, DELETE 등)를 허용
                .allowedHeaders("*")  // 모든 헤더를 허용
                .allowCredentials(true)  // 쿠키 전송 허용 (특정 도메인 지정 시에만 true로 설정 가능)
                .exposedHeaders("Authorization", "Set-Cookie");  // 클라이언트에서 접근 가능한 헤더 설정
    }
}