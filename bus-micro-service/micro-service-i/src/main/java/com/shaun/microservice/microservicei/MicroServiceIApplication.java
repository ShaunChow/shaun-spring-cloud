package com.shaun.microservice.microservicei;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@EnableCircuitBreaker
@SpringBootApplication
public class MicroServiceIApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroServiceIApplication.class, args);
	}

}
