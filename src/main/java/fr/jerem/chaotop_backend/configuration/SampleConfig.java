package fr.jerem.chaotop_backend.configuration;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.passay.*;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;

import fr.jerem.chaotop_backend.model.DataBaseEntityUser;
import fr.jerem.chaotop_backend.model.RentalEntity;
import fr.jerem.chaotop_backend.repository.RentalRepository;
import fr.jerem.chaotop_backend.repository.UserRepository;

/**
 * Configures sample data:
 *  - sample User and their encrypted password
 *  - sample rentals
 *  - sample messages
 * <p>
 * Each CommandLineRunner is ordered using Order(x) annotation
 */


@Configuration
public class SampleConfig {


    /**
     * Create a set of sample users
     * 
     * @param userRepository
     * @param passwordEncoder
     * @return {@link CommandLineRunner}
     */
    @Bean
    @Order(1)
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
     * Generate a random password using Passay library
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

     /**
     * Create a set of Rentals
     * 
     * @param userRepository
     * @param passwordEncoder
     * @return {@link CommandLineRunner}
     */
    @Bean
    @Order(2)
    public CommandLineRunner createSampleRentals(RentalRepository rentalRepository, UserRepository userRepository) {
        return args -> {
            RentalEntity rental = new RentalEntity();

            rental.setName("rental1");
            rental.setSurface(432);
            rental.setPrice(new BigDecimal(300));
            rental.setPicture("https://blog.technavio.org/wp-content/uploads/2018/12/Online-House-Rental-Sites.jpg");
            rental.setDescription(
                    "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam a lectus eleifend, varius massa ac, mollis tortor. Quisque ipsum nulla, faucibus ac metus a, eleifend efficitur augue. Integer vel pulvinar ipsum. Praesent mollis neque sed sagittis ultricies. Suspendisse congue ligula at justo molestie, eget cursus nulla tincidunt. Pellentesque elementum rhoncus arcu, viverra gravida turpis mattis in. Maecenas tempor elementum lorem vel ultricies. Nam tempus laoreet eros, et viverra libero tincidunt a. Nunc vel nisi vulputate, sodales massa eu, varius erat.");
            rental.setOwner(userRepository.findByEmail("bob@mail.tld"));
            rental.setCreatedAt(LocalDateTime.now());
            rental.setUpdatedAt(LocalDateTime.now());

            rentalRepository.save(rental);
        };

    }
}
