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

    @Bean
    @Order(2)
    public CommandLineRunner createSampleRentals(RentalRepository rentalRepository, UserRepository userRepository) {
        return args -> {
            RentalEntity rental1 = new RentalEntity();

            rental1.setName("rental 1");
            rental1.setSurface(153);
            rental1.setPrice(new BigDecimal(350));
            rental1.setPicture(
                    "https://res.cloudinary.com/diqyy0y8d/image/upload/v1738317625/ddjqm1dtdbempi0v5rpl.webp");
            rental1.setDescription(
                    "Découvrez ce bel appartement offrant une vue imprenable sur le parc. " +
                            "Idéal pour les amateurs de tranquillité, il dispose de deux chambres " +
                            "spacieuses de 12m² et 15m², parfaites pour un We en couple, en famille " +
                            "ou entre amis. Lumineux et bien agencé, cet espace vous séduira par son " +
                            "charme et son confort.");
            rental1.setOwner(userRepository.findByEmail("jsmith@ms.com"));
            rental1.setCreatedAt(LocalDateTime.now());
            rental1.setUpdatedAt(LocalDateTime.now());

            rentalRepository.save(rental1);

            RentalEntity rental2 = new RentalEntity();

            rental2.setName("rental 2");
            rental2.setSurface(120);
            rental2.setPrice(new BigDecimal(95));
            rental2.setPicture(
                    "https://res.cloudinary.com/diqyy0y8d/image/upload/v1738316363/um73ftudw9nfttqjoxh8.webp");
            rental2.setDescription(
                    "Découvrez cette jolie maison de lotissement offrant trois chambres, un salon " +
                            "avec accès sur un jardin cloturé et arboré. Un garage vous permettra de ranger jusqu'à " +
                            "deux voitures. Idéal pour séjour famillial.");
            rental2.setOwner(userRepository.findByEmail("bob@mail.tld"));
            rental2.setCreatedAt(LocalDateTime.now());
            rental2.setUpdatedAt(LocalDateTime.now());

            rentalRepository.save(rental2);

        };

    }
}
