package org.example.auctify.controller.oauth2;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

// 테스트용 회원가입한 사용자만 허용
@Controller
public class MyController {

    @GetMapping("/my")
    @ResponseBody
    public String myAPI(@AuthenticationPrincipal UserDetails userDetails) {
        System.out.println(userDetails.getUsername());

        return "my route";
    }

}
