package fr.jerem.chaotop_backend.configuration;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.passay.*;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;

import fr.jerem.chaotop_backend.model.UserEntity;
import fr.jerem.chaotop_backend.model.RentalEntity;
import fr.jerem.chaotop_backend.repository.RentalRepository;
import fr.jerem.chaotop_backend.repository.UserRepository;

/**
 * Configures sample data:
 * - sample User and their encrypted password
 * - sample rentals
 * - sample messages
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
                if (userRepository.findByEmail(sampleUserMail).isEmpty()) {
                    String sampleUserclearPassword = generateSecurePassword();
                    String sampleUserhashedPassword = passwordEncoder.encode(sampleUserclearPassword);

                    UserEntity sampleEntityUser = new UserEntity();
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
            LocalDateTime now = LocalDateTime.now();

            List<RentalEntity> rentals = List.of(
                    new RentalEntity("Bel appartement dans quartier chic", 153, new BigDecimal(250),
                            "https://res.cloudinary.com/diqyy0y8d/image/upload/v1738317625/ddjqm1dtdbempi0v5rpl.webp",
                            "Découvrez ce bel appartement offrant une vue imprenable sur le parc. " +
                                    "Idéal pour les amateurs de tranquillité, il dispose de deux chambres " +
                                    "spacieuses de 12m² et 15m², parfaites pour un week-end en couple, en famille " +
                                    "ou entre amis. Lumineux et bien agencé, cet espace vous séduira par son " +
                                    "charme et son confort.",
                            userRepository.findByEmail("jsmith@ms.com").get(),
                            now,
                            now),
                    new RentalEntity("Appartement moderne et spacieux ", 100, new BigDecimal(95),
                            "https://res.cloudinary.com/diqyy0y8d/image/upload/v1738334832/ptfbksijtpfctjsgb0sg.jpg",
                            "Découvrez ce joli petit appartement offrant une cuisine ouverte sur salon avec parquet " +
                                    "avec accès sur balcon. Situé dans un quartier calme, le logement saura vous séduire... ",
                            userRepository.findByEmail("bob@mail.tld").get(),
                            now,
                            now),
                    new RentalEntity("Villa de luxe", 450, new BigDecimal(1000),
                            "https://res.cloudinary.com/diqyy0y8d/image/upload/v1738334725/suignxuixv5urvc3hz71.png",
                            "Découvrez cette magnifique villa avec piscine, 8 chambres, 3 salles de bain, 3 salons " +
                                    "avec accès sur un parc magnifiquement arboré. Vous disposerez également d'un spa, d'un hamam, "
                                    +
                                    "d'une salle de jeux avec billard, baby-foot, tables de jeux, d'une bibliothèque...",
                            userRepository.findByEmail("mmoore@great.com").get(),
                            now,
                            now));
            rentals.forEach(rental -> {
                rentalRepository.save(rental);
            });

        };
    }
}
