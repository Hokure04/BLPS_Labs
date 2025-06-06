package org.example.blps_lab1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableTransactionManagement
@SpringBootApplication
@EnableWebMvc
@EnableScheduling
@EnableRetry
@EnableJpaRepositories
public class LearningPlatform {

	public static void main(String[] args) {
		SpringApplication.run(LearningPlatform.class, args);
	}

}