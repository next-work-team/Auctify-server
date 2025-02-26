package org.example.auctify.controller.oauth2;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

// 테스트용 회원가입한 사용자만 허용
@Controller
public class MyController {

    @GetMapping("/my")
    @ResponseBody
    public String myAPI() {

        return "my route";
    }

}
