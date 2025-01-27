package fr.jerem.chaotop_backend.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import fr.jerem.chaotop_backend.configuration.properties.AppConfigProperties;
import fr.jerem.chaotop_backend.repository.UserRepository;
import fr.jerem.chaotop_backend.service.JwtFactory;
import fr.jerem.chaotop_backend.service.UserManagementService;
import fr.jerem.chaotop_backend.service.DefaultUserManagementService;
import fr.jerem.chaotop_backend.service.HmacJwtFactory;

@Configuration
public class AppConfig {

    @Bean
    public JwtFactory jwtFactory(AppConfigProperties appConfigProperties) {
        return new HmacJwtFactory(appConfigProperties.getJwtsecretkey());
    }

    @Bean
    public UserManagementService userManagementService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return new DefaultUserManagementService(userRepository, passwordEncoder);
    }
}