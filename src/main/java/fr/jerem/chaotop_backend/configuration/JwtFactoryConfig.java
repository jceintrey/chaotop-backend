package fr.jerem.chaotop_backend.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import fr.jerem.chaotop_backend.configuration.properties.AppConfigProperties;
import fr.jerem.chaotop_backend.service.JwtFactory;
import fr.jerem.chaotop_backend.service.HmacJwtFactory;

@Configuration
public class JwtFactoryConfig {

    @Bean
    public JwtFactory jwtFactory(AppConfigProperties appConfigProperties) {
        // Instancie la factory en utilisant la clé de la configuration
        return new HmacJwtFactory(appConfigProperties.getJwtsecretkey());
    }
}