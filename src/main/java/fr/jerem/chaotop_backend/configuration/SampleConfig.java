package fr.jerem.chaotop_backend.configuration;

import java.time.LocalDateTime;
import java.util.Arrays;
import org.passay.*;

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
            String sampleUserMail = "sample@example.com";
            String sampleUserName = "Sample User";
            // Check if user Already exist
            if (userRepository.findByEmail(sampleUserMail) == null) {
                String sampleUserclearPassword = generateSecurePassword();
                String sampleUserhashedPassword = passwordEncoder.encode(sampleUserclearPassword);

                DataBaseEntityUser sampleUser = new DataBaseEntityUser();
                sampleUser.setEmail(sampleUserMail);
                sampleUser.setPassword(sampleUserhashedPassword);
                sampleUser.setName(sampleUserName);
                sampleUser.setCreatedAt(LocalDateTime.now());
                sampleUser.setUpdatedAt(LocalDateTime.now());

                userRepository.save(sampleUser);
                System.out.println("Sample user " + sampleUserMail + ", has been created with the password "
                        + sampleUserclearPassword);
            } else {
                System.out.println("Sample user" + sampleUserMail + " already exists.");
            }
        };
    }

    private String generateSecurePassword() {
        PasswordGenerator generator = new PasswordGenerator();
        CharacterRule lowerCaseRule = new CharacterRule(EnglishCharacterData.LowerCase, 2);
        CharacterRule upperCaseRule = new CharacterRule(EnglishCharacterData.UpperCase, 2);
        CharacterRule digitRule = new CharacterRule(EnglishCharacterData.Digit, 2);

        // Longueur minimale du mot de passe
        int passwordLength = 12;

        return generator.generatePassword(passwordLength,
                Arrays.asList(lowerCaseRule, upperCaseRule, digitRule));
    }
}
