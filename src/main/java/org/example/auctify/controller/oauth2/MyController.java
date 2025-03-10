package org.example.auctify.controller.oauth2;

import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

// 테스트용 회원가입한 사용자만 허용
@Controller
@Slf4j
public class MyController {

    @GetMapping("/my")
    @ResponseBody
    public String myAPI(@AuthenticationPrincipal UserDetails userDetails) {

        System.out.println(userDetails.getUsername());
        log.info("[LOG]  : "+userDetails.getUsername());

        return "my route";
    }

}
