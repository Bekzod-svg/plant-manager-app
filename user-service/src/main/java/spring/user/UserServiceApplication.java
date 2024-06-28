package spring.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import spring.user.entity.User;
import spring.user.repository.UserRepository;

@SpringBootApplication
@EnableDiscoveryClient
@Slf4j
public class UserServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
	}

	@Bean
	public CommandLineRunner createDemoData(UserRepository repository) {
		return args -> {
			if (repository.count() < 2) {
				log.info("Generating dummy data for user service...");
				repository.save(new User(1L, "user1", "password1", "user1@example.com", "name1", "surname1"));
				repository.save(new User(2L, "user2", "password2", "user2@example.com", "name2", "surname2"));
				log.info("Finish generating dummy data for user service...");
			}
		};
	}


}
