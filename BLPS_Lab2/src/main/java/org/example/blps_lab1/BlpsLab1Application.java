package org.example.blps_lab1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableTransactionManagement
@SpringBootApplication
@EnableWebMvc
@EnableJpaRepositories
public class BlpsLab1Application {

	public static void main(String[] args) {
		SpringApplication.run(BlpsLab1Application.class, args);
	}

}

