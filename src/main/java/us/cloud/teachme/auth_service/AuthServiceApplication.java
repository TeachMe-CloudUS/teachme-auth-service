package us.cloud.teachme.auth_service;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import us.cloud.teachme.auth_service.model.User;
import us.cloud.teachme.auth_service.repository.UserRepository;

@SpringBootApplication
@PropertySource("classpath:security.properties")
public class AuthServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthServiceApplication.class, args);
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	@ConditionalOnMissingBean(name = "kafkaTemplate")
	KafkaTemplate<String, Object> kafkaTemplate() {
		return null;
	}

	@Bean
	CommandLineRunner init(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		return args -> {
			List<User> users = userRepository.findByEmail("admin@teachme.com");
			if (users.isEmpty()) {
				User user = User.builder().email("admin@gmail.com").password(passwordEncoder.encode("123456")).role("ADMIN")
						.enabled(true).build();
				userRepository.save(user);
			} else {
				User user = users.get(0);
				user.setRole("ADMIN");
				userRepository.save(user);
			}
		};
	}
}
