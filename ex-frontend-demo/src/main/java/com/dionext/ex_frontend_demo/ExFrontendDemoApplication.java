package com.dionext.ex_frontend_demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@Slf4j

@ComponentScan
@ComponentScan(basePackages = {"com.dionext"})
@EnableJpaRepositories(basePackages = "com.dionext")
@EntityScan(basePackages = "com.dionext")
public class ExFrontendDemoApplication {
	private static ApplicationContext applicationContext;
	public static void main(String[] args) {
		log.info("----------------- start");
		applicationContext =
				new AnnotationConfigApplicationContext(ExFrontendDemoApplication.class);
		for (String beanName : applicationContext.getBeanDefinitionNames()) {
			System.out.println(beanName);
		}

		SpringApplication.run(ExFrontendDemoApplication.class, args);
	}
	private static void checkBeansPresence(String... beans) {
		for (String beanName : beans) {
			System.out.println("Is " + beanName + " in ApplicationContext: " +
					applicationContext.containsBean(beanName));
		}
	}
}
