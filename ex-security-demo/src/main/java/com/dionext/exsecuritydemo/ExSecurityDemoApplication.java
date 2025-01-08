package com.dionext.exsecuritydemo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.util.UriUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@SpringBootApplication
@Slf4j
public class ExSecurityDemoApplication {

	public static void main(String[] args) {
		String s = "aaa sss+ppp";
        try {
           String s1 =URLEncoder.encode(s, "UTF-8");
			String s2 =UriUtils.encodePath(s, StandardCharsets.UTF_8.toString());
			String a = "sss";
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        SpringApplication.run(ExSecurityDemoApplication.class, args);
	}

}
