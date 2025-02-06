package fr.jerem.chaotop_backend.configuration;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import fr.jerem.chaotop_backend.configuration.properties.AppConfigProperties;
import fr.jerem.chaotop_backend.dto.RentalResponse;
import fr.jerem.chaotop_backend.model.RentalEntity;
import fr.jerem.chaotop_backend.repository.UserRepository;
import fr.jerem.chaotop_backend.service.JwtFactory;
import fr.jerem.chaotop_backend.service.StorageService;
import fr.jerem.chaotop_backend.service.UserManagementService;

import fr.jerem.chaotop_backend.service.CloudinaryStorageService;
import fr.jerem.chaotop_backend.service.DefaultUserManagementService;
import fr.jerem.chaotop_backend.service.HmacJwtFactory;

/**
 * Configuration class for the application.
 * This class defines beans that will be managed by the Spring application.
 */
@Configuration
public class AppConfig {

    /**
     * Defines a StorageService bean responsible for files/pictures storage.
     * Here we uses a StorageService implementation : Cloudinary.
     * The service is configured with properties from AppConfigProperties.
     *
     * @param appConfigProperties Configuration properties for the Storage service
     * @return a StorageService specific implementation
     */
    @Bean
    public StorageService storageService(AppConfigProperties appConfigProperties) {
        return new CloudinaryStorageService(
                appConfigProperties.getCloudinarycloudname(),
                appConfigProperties.getCloudinaryapikey(),
                appConfigProperties.getCloudinaryapisecret());
    }

    /**
     * Defines a JwtFactory bean for handling JSON Web Token (JWT) creation and
     * validation.
     * Here we uses an HMAC-based JWT factory configured with properties from
     * AppConfigProperties.
     *
     * @param appConfigProperties Configuration properties
     * @return a JwtFactory specific implementation
     */
    @Bean
    public JwtFactory jwtFactory(AppConfigProperties appConfigProperties) {
        return new HmacJwtFactory(appConfigProperties.getJwtsecretkey());
    }

    /**
     * Defines a UserManagementService bean that handles user management operations.
     * Here we uses a UserManagementService implementation : DefaultUserManagementService.
     *
     * @param userRepository  Repository for accessing user data.
     * @param passwordEncoder Encoder for hashing passwords.
     * @return a UserManagementService specific implementation
     */
    @Bean
    public UserManagementService userManagementService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return new DefaultUserManagementService(userRepository, passwordEncoder);
    }

    /**
     * Defines a ModelMapper bean used to convert entity objects to DTOs (vice-versa)
     *
     * @return A configured ModelMapper instance.
     */
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        // Custom mapping for RentalResponse
        modelMapper.addMappings(new PropertyMap<RentalEntity, RentalResponse>() {
            @Override
            protected void configure() {
                map().setOwner(source.getOwner().getId());
            }
        });

        return modelMapper;
    }
}