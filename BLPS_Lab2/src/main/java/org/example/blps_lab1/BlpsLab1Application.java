package org.example.blps_lab1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableScheduling
@EnableTransactionManagement
@SpringBootApplication
public class BlpsLab1Application {

	public static void main(String[] args) {
		System.setProperty("java.naming.factory.initial", "org.jboss.narayana.jta.boot.naming.NarayanaInitialContextFactory");
		SpringApplication.run(BlpsLab1Application.class, args);
	}

}
