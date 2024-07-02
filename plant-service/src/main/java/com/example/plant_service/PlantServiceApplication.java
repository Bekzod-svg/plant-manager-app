package com.example.plant_service;

import com.example.plant_service.model.*;
import com.example.plant_service.repository.HydrogenInstallationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.Date;

@SpringBootApplication
@EnableDiscoveryClient
@Slf4j
public class PlantServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PlantServiceApplication.class, args);
	}


	@Bean
	public CommandLineRunner createDemoData(HydrogenInstallationRepository hydrogenInstallationRepository) {
		return args -> {
			if (hydrogenInstallationRepository.count() < 2) {
				log.info("Generating dummy data for plant service...");

				User user1 = new User(1L, "user1", "password1", "user1@example.com", "name1", "surname1");
				HydrogenInstallation hydrogenInstallation1 = new HydrogenInstallation(1L, StatusType.INACTIVE, new Location(1.0,2.0), 1L, user1, new ArrayList<>());
				hydrogenInstallation1.getHistoricalDates().add(new HistoricalDate(HistoricalDateType.CREATED, new Date()));
				hydrogenInstallationRepository.save(hydrogenInstallation1);

				User user2 = new User(2L, "user2", "password2", "user2@example.com", "name2", "surname2");
				HydrogenInstallation hydrogenInstallation2 = new HydrogenInstallation(2L, StatusType.INACTIVE, new Location(1.0,2.0), 2L, user2, new ArrayList<>());
				hydrogenInstallation2.getHistoricalDates().add(new HistoricalDate(HistoricalDateType.CREATED, new Date()));
				hydrogenInstallationRepository.save(hydrogenInstallation2);

				log.info("Finish generating dummy data for plant service...");
			}
		};
	}
}
