package fr.jerem.chaotop_backend.configuration;

import java.time.LocalDateTime;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import fr.jerem.chaotop_backend.model.DataBaseEntityUser;
import fr.jerem.chaotop_backend.repository.UserRepository;

/**
 * Configures sample data like a sample User and their encrypted password
 * <p>
 */
@Configuration
public class SampleConfig {

    @Bean
    public CommandLineRunner createSampleUser(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Vérifie si l'utilisateur sample existe déjà
            if (userRepository.findByEmail("sample@example.com") == null) {
                DataBaseEntityUser sampleUser = new DataBaseEntityUser();
                sampleUser.setEmail("sample@example.com"); // Email comme identifiant
                sampleUser.setPassword(passwordEncoder.encode("samplepassword")); // Mot de passe haché avec BCrypt
                sampleUser.setName("Sample User");
                sampleUser.setCreatedAt(LocalDateTime.now());
                sampleUser.setUpdatedAt(LocalDateTime.now());

                userRepository.save(sampleUser); // Sauvegarde dans la base
                System.out.println("Sample user created: sample@example.com / samplepassword");
            } else {
                System.out.println("Sample user already exists.");
            }
        };
    }
}
