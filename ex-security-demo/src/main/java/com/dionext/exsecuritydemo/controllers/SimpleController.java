package com.dionext.exsecuritydemo.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SimpleController {
    @GetMapping("/*")
    public String all(){

        return "all";
    }
    @GetMapping("/test/test")
    public String testtest(){

        return "testtest";
    }
    @GetMapping("/test")
    public String test(){

        return "test";
    }
}
