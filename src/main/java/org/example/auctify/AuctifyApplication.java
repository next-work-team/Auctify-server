package org.example.auctify;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaAuditing //Auditing기능을 위한 코드
@EnableScheduling // 스케줄링 활성화
public class AuctifyApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuctifyApplication.class, args);
    }

}
