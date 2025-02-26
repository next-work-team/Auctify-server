package org.example.auctify;

import org.example.auctify.jwt.JWTUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = "spring.jwt.secret=test-secret")
class AuctifyApplicationTests {


    @Test
    void contextLoads() {
    }

}
