package com.dionext.ex_spring_batch_demo.controllers;



import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping //(value = {"/admin"})
public class BatchController {

    @GetMapping("/index")
    public ResponseEntity<String> index() throws Exception {

        String str = MessageFormat.format("""
                <div hx-target="this" hx-swap="outerHTML">
                  <h3>Start Progress</h3>
                  <button class="btn primary" hx-post="/start">
                            Start Job
                  </button>
                </div>
                """, ""
        );
        return sendOk(str);
    }
    protected ResponseEntity<String> sendOk(String data) {
        return sendOk(data, null);
    }
    protected ResponseEntity<String> sendOk(String data, HttpHeaders responseHeaders) {
        if (responseHeaders == null) responseHeaders = new HttpHeaders();
        responseHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_HTML_VALUE + "; charset=utf-8");

        String style = """
                .progress {
                    height: 20px;
                    margin-bottom: 20px;
                    overflow: hidden;
                    background-color: #f5f5f5;
                    border-radius: 4px;
                    box-shadow: inset 0 1px 2px rgba(0,0,0,.1);
                }
                .progress-bar {
                    float: left;
                    width: 0%;
                    height: 100%;
                    font-size: 12px;
                    line-height: 20px;
                    color: #fff;
                    text-align: center;
                    background-color: #337ab7;
                    -webkit-box-shadow: inset 0 -1px 0 rgba(0,0,0,.15);
                    box-shadow: inset 0 -1px 0 rgba(0,0,0,.15);
                    -webkit-transition: width .6s ease;
                    -o-transition: width .6s ease;
                    transition: width .6s ease;
                }

                """;

        String html = MessageFormat.format("""
                <!doctype html>
                <html lang="en">
                <style>
                {0}
                </style>
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport"
                          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
                    <meta http-equiv="X-UA-Compatible" content="ie=edge">
                    <title>HelloX</title>
                    <script src="https://unpkg.com/htmx.org@1.9.12" integrity="sha384-ujb1lZYygJmzgSwoxRggbCHcjc0rB2XoQrxeTUQyRjrOnlCoYta87iKBWq3EsdM2" crossorigin="anonymous"></script>
                </head>
                <body>
                {1}
                </body>
                </html>
                """, style, data);


        return new ResponseEntity<>(html, responseHeaders, HttpStatus.OK);
    }
    protected ResponseEntity<String> sendFragment(String data) {
        return sendFragment(data);
    }
    protected ResponseEntity<String> sendFragment(String data, HttpHeaders responseHeaders) {
        if (responseHeaders == null) responseHeaders = new HttpHeaders();
        responseHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_HTML_VALUE + "; charset=utf-8");
        return new ResponseEntity<>(data, responseHeaders, HttpStatus.OK);
    }




}
