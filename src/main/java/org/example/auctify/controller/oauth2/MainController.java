package org.example.auctify.controller.oauth2;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;


// 테스트용 모두 허용
@Controller
public class MainController {

    @GetMapping("/")
    @ResponseBody
    public String mainAPI(){

        return "main router";
    }


}
