package com.bankapp.messagerouter;

import com.bankapp.messagerouter.service.MqService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(scanBasePackages = "com.bankapp.messagerouter")
@ComponentScan(basePackages = {"com.bankapp.messagerouter.service","com.bankapp.messagerouter.controller","com.bankapp.messagerouter.entity","com.bankapp.messagerouter.repository","com.bankapp.messagerouter"})
public class MessageRouterApplication {

	public static void main(String[] args) {
//		System.setProperty("java.library.path", "/opt/mqm");
		System.setProperty("java.library.path", "/opt/mqm/java/lib64");
		SpringApplication.run(MessageRouterApplication.class, args);
	}

	@Bean
	public CommandLineRunner run(MqService mqService) {
		return args -> {
			try {
				mqService.sendMessage("Hello, this is a test message.");
			} catch (Exception e) {
				e.printStackTrace();
			}
		};
	}

}
