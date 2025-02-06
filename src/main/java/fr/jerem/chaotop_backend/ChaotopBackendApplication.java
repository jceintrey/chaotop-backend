package fr.jerem.chaotop_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import fr.jerem.chaotop_backend.configuration.properties.AppConfigProperties;

/**
 * Main class for ChaotopBackend Spring Boot application.
 * 
 * <p>
 * This class is responsible for bootstrapping the Spring Boot application and
 * loading the necessary configuration properties defined in
 * {@link AppConfigProperties}.
 * </p>
 */
@SpringBootApplication
@EnableConfigurationProperties(AppConfigProperties.class)
public class ChaotopBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChaotopBackendApplication.class, args);
	}

}
