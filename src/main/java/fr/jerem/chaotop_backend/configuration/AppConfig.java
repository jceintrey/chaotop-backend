package fr.jerem.chaotop_backend.configuration;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

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

@Configuration
public class AppConfig {

    @Bean
    public StorageService storageService(AppConfigProperties appConfigProperties) {
        return new CloudinaryStorageService(
                appConfigProperties.getCloudinarycloudname(),
                appConfigProperties.getCloudinaryapikey(),
                appConfigProperties.getCloudinaryapisecret());
    }

    @Bean
    public JwtFactory jwtFactory(AppConfigProperties appConfigProperties) {
        return new HmacJwtFactory(appConfigProperties.getJwtsecretkey());
    }

    @Bean
    public UserManagementService userManagementService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return new DefaultUserManagementService(userRepository, passwordEncoder);
    }

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