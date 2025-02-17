package org.example.auctify.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloTestController {


    @GetMapping("/")
    public String test(){
        return "hello  test123123";
    }
}
