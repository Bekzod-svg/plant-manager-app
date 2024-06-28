package com.example.plant_service;

import com.example.plant_service.model.HydrogenInstallation;
import com.example.plant_service.model.Location;
import com.example.plant_service.model.StatusType;
import com.example.plant_service.model.User;
import com.example.plant_service.repository.HydrogenInstallationRepository;
import com.example.plant_service.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
@Slf4j
public class PlantServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PlantServiceApplication.class, args);
	}


	@Bean
	public CommandLineRunner createDemoData(HydrogenInstallationRepository hydrogenInstallationRepository, UserRepository userRepository) {
		return args -> {
			if (hydrogenInstallationRepository.count() < 2) {
				log.info("Generating dummy data for plant service...");

				User user1 = userRepository.findById(1L).orElse(null);
				if(user1 == null) {
					user1 = new User(1L, "user1", "password1", "user1@example.com", "name1", "surname1");
					userRepository.save(user1);
				}
				hydrogenInstallationRepository.save(new HydrogenInstallation(1L, StatusType.INACTIVE, new Location(1.0,2.0), user1, null));

				User user2 = userRepository.findById(2L).orElse(null);
				if(user2 == null) {
					user2 = new User(2L, "user2", "password2", "user2@example.com", "name2", "surname2");
					userRepository.save(user2);
				}
				hydrogenInstallationRepository.save(new HydrogenInstallation(2L, StatusType.INACTIVE, new Location(1.0,2.0), user2, null));

				log.info("Finish generating dummy data for plant service...");
			}
		};
	}
}
