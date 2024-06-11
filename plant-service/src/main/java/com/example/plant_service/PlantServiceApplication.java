package com.example.plant_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class PlantServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PlantServiceApplication.class, args);
	}

}
