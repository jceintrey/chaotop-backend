package fr.jerem.chaotop_backend.configuration;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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
    /**
     * Responsible of users creation
     * 
     * @param userRepository
     * @param passwordEncoder
     * @return
     */
    @Bean
    public CommandLineRunner createSampleUser(UserRepository userRepository, PasswordEncoder passwordEncoder) {

        HashMap<String, String> userMap = new HashMap<>();
        userMap.put("bob@mail.tld", "bob");
        userMap.put("jsmith@ms.com", "John Smith");
        userMap.put("mmoore@great.com", "Michael Moore");

        return args -> {

            for (Map.Entry<String, String> entry : userMap.entrySet()) {

                String sampleUserMail = entry.getKey();
                String sampleUserName = entry.getValue();

                // Check if user Already exist
                if (userRepository.findByEmail(sampleUserMail) == null) {
                    String sampleUserclearPassword = generateSecurePassword();
                    String sampleUserhashedPassword = passwordEncoder.encode(sampleUserclearPassword);

                    DataBaseEntityUser sampleEntityUser = new DataBaseEntityUser();
                    sampleEntityUser.setEmail(sampleUserMail);
                    sampleEntityUser.setPassword(sampleUserhashedPassword);
                    sampleEntityUser.setName(sampleUserName);
                    sampleEntityUser.setCreatedAt(LocalDateTime.now());
                    sampleEntityUser.setUpdatedAt(LocalDateTime.now());

                    userRepository.save(sampleEntityUser);
                    System.out.println("Sample user " + sampleUserMail + ", has been created with the password "
                            + sampleUserclearPassword);
                } else {
                    System.out.println("Sample user" + sampleUserMail + " already exists.");
                }
            }
        };
    }

    /**
     * The method generate a random password
     * 
     * @return String generated password
     */
    private String generateSecurePassword() {
        PasswordGenerator generator = new PasswordGenerator();
        CharacterRule lowerCaseRule = new CharacterRule(EnglishCharacterData.LowerCase, 2);
        CharacterRule upperCaseRule = new CharacterRule(EnglishCharacterData.UpperCase, 2);
        CharacterRule digitRule = new CharacterRule(EnglishCharacterData.Digit, 2);

        int passwordLength = 12;

        return generator.generatePassword(passwordLength,
                Arrays.asList(lowerCaseRule, upperCaseRule, digitRule));
    }
}
