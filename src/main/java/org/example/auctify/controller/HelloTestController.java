package org.example.auctify.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// 순수 테스트를 위한 Controller 추후에 삭제예정
@RestController
public class HelloTestController {


    @GetMapping("/test")
    public String test(){
        return "hello  testKkk";
    }
}
