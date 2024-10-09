package com.example.emailSenderApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.example.emailSenderApp")
public class EmailSenderAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmailSenderAppApplication.class, args);
	}

}
