package fr.jerem.chaotop_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;


import fr.jerem.chaotop_backend.configuration.properties.AppConfigProperties;


@SpringBootApplication
@EnableConfigurationProperties(AppConfigProperties.class)

public class ChaotopBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChaotopBackendApplication.class, args);
	}

}
