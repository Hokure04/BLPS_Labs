package org.example.blps_lab1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@EnableTransactionManagement
@SpringBootApplication
@EnableWebMvc
public class LearningPlatform extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(LearningPlatform.class, args);
	}
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(LearningPlatform.class);
	}

	private static Class<LearningPlatform> applicationClass = LearningPlatform.class;
}

