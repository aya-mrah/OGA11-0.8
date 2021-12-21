package com.oga.userservice;

import com.oga.userservice.Entity.UserEntity;
import com.oga.userservice.dto.FileStorageProperties;
import com.oga.userservice.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

@SpringBootApplication
@EnableEurekaClient
@EnableWebSecurity
@EnableConfigurationProperties({
		FileStorageProperties.class
})
public class UserServiceApplication {

	@Bean
	@LoadBalanced
	public RestTemplate restTemplate(){
		return new RestTemplate();
	}

//	@Bean
//	public WebClient.Builder webClient(){
//		return WebClient.builder();
//	}
@Autowired
UserService userService;

	@PostConstruct
	public String initRh(){


		UserEntity rh = new UserEntity();
		rh.setId(1);
		rh.setEmail("mouez");
		rh.setRole("Responsable");
		rh.setDepartement("Administration");
		rh.setPassword("admin");
		rh.setTelephone("21614264");
		rh.setUserName(rh.getEmail());
		rh.setPrenom("Mouez");
		rh.setNom("Chouchene");

		rh.getUserName();
		UserEntity userExist = userService.getUserByUserName(rh.getUserName());


		if (userExist==null){
			userService.addUser(rh);

			return "new user add to database";
		}
		else return "this user already exist";


	}

	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
	}

}
