package fr.jerem.chaotop_backend.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import fr.jerem.chaotop_backend.dto.UserProfileResponse;
import fr.jerem.chaotop_backend.model.AppUserDetails;
import fr.jerem.chaotop_backend.model.DataBaseEntityUser;
import fr.jerem.chaotop_backend.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;

/**
 * UserService class for managing users
 * 
 * <p>
 * This class provides functionality for creating new users and handling user
 * data.
 * It interacts with the {@link UserRepository} for persistence and uses a
 * {@link PasswordEncoder} to store passwords.
 * </p>
 * 
 * 
 */
@Service
@Slf4j
public class DefaultUserManagementService implements UserManagementService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DefaultUserManagementService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public AppUserDetails createUser(String email, String plainPassword, String name) {

        DataBaseEntityUser user = new DataBaseEntityUser();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(plainPassword));
        user.setName(name);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);

        return new AppUserDetails(user);
    }

    @Override
    public Optional<UserDetails> getUserbyEmail(String email) {
        return Optional.ofNullable(new AppUserDetails(this.userRepository.findByEmail(email)));
    }

    @Override
    public Optional<Long> getUserId(String email) {
        return Optional.ofNullable(this.userRepository.findByEmail(email))
                .map(user -> Long.valueOf(user.getId().longValue()));
    }

    /**
     * Get the user Informations and build a {@link UserProfileResponse}
     * 
     * @param String email of the user.
     * 
     * @return {@link UserProfileResponse} the DTO response containing the user
     *         informations.
     * 
     */
    @Override
    public UserProfileResponse getUserInformationResponse(String email) {
        Optional<UserDetails> userDetailsOptional = getUserbyEmail(email);

        // if user is present
        return userDetailsOptional.map(userDetails -> {
            // Cast to AppUserDetails to get detail user fields
            AppUserDetails appUserDetails = (AppUserDetails) userDetails;
            return new UserProfileResponse(
                    appUserDetails.getId(),
                    appUserDetails.getName(),
                    appUserDetails.getEmail(),
                    appUserDetails.getCreatedAt().toString(),
                    appUserDetails.getUpdatedAt().toString());
        }).orElseGet(() -> {
            // return empty UserProfileResponse if user not found
            return new UserProfileResponse(null, "", "", "", "");
        });
    }

    @Override
    public boolean isEmailAlreadyUsed(String email) {

        Optional<Long> optionalUserId = getUserId(email);

        return optionalUserId.isPresent();
    }

    @Override
    public Optional<DataBaseEntityUser> getUserById(Long userId) {

        try {
            Integer userIdInteger = Math.toIntExact(userId);
            return userRepository.findById(userIdInteger);
        } catch (ArithmeticException e) {
            throw new IllegalArgumentException("userId is out of bounds for Integer: " + userId, e);
        }
    }

}
