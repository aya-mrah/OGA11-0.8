package com.oga.discoveruservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class DiscoveruServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(DiscoveruServiceApplication.class, args);
	}

}
