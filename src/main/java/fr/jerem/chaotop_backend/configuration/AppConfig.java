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
import org.springframework.beans.factory.annotation.Value;

import fr.jerem.chaotop_backend.service.CloudinaryStorageService;
import fr.jerem.chaotop_backend.service.DefaultUserManagementService;
import fr.jerem.chaotop_backend.service.HmacJwtFactory;

@Configuration
public class AppConfig {
    @Value("${cloudinary.cloud_name}")
    private String cloudinaryCloudName;

    @Value("${cloudinary.api_key}")
    private String cloudinaryApiKey;

    @Value("${cloudinary.api_secret}")
    private String cloudinaryApiSecret;

    @Bean
    public StorageService storageService() {
        return new CloudinaryStorageService(cloudinaryCloudName, cloudinaryApiKey, cloudinaryApiSecret);
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