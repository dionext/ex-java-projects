package com.dionext.exoauth2demo.controllers;


import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;


@RestController
@Slf4j
//@RequiredArgsConstructor
public class MainController {

    @GetMapping(value = {"/public/test"}, produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public ResponseEntity<String> publictest() {

        return new ResponseEntity("OK public test ", HttpStatus.OK);
    }

    @GetMapping(value = {"/*"}, produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public ResponseEntity<String> alltest() {

        return new ResponseEntity("OK all test ", HttpStatus.OK);
    }

    @GetMapping(value = {"/api/test"}, produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> apitest() {
        return new ResponseEntity("OK api test ", HttpStatus.OK);
    }

    @GetMapping(value = {"/admin/test"}, produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> admintest() {
        return new ResponseEntity("OK admin test ", HttpStatus.OK);
    }

    @GetMapping(value = {"/admin/main"}, produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> maintest() {
        return new ResponseEntity("OK admin test ", HttpStatus.OK);
    }

    @GetMapping("logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) throws Exception {
        request.logout();
        return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY).location(
                URI.create("main")).build();
    }


}
