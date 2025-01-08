package com.dionext.exkafkaclient;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@Slf4j
@EnableScheduling
public class ExKafkaClientApplication {

	public static void main(String[] args) {
		log.info("==========================================");
		log.info("============ 1  ===============");
		log.info("==========================================");
		SpringApplication.run(ExKafkaClientApplication.class, args);
	}

}
