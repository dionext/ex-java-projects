package com.dionext.ex_frontend_demo.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Slf4j
public class IndexController {

    @GetMapping("/index2")
    public String index() {
        log.info("-----------------1");
        return "Hello, this is the index endpoint!";
    }
} 