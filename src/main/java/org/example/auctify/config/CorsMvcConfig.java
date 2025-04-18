package org.example.auctify.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsMvcConfig implements WebMvcConfigurer {

    private static final String LOCAL_ORIGIN = "https://localhost:3000";
    private static final String PROD_ORIGIN = "https://www.auctify.shop";

    @Autowired
    private Environment env;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 활성화된 프로파일 목록 중에 "local" 이 있으면 로컬, 아니면 프로덕션
        String[] activeProfiles = env.getActiveProfiles();
        String[] allowedOrigins;
        if (Arrays.asList(activeProfiles).contains("local")) {
            allowedOrigins = new String[]{ LOCAL_ORIGIN };
        } else {
            allowedOrigins = new String[]{ PROD_ORIGIN };
        }

        registry.addMapping("/**")
                // spring 6.2+ 에선 allowedOrigins 대신 patterns 권장
                .allowedOriginPatterns(allowedOrigins)
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(true)
                .exposedHeaders("Authorization");
    }
}
