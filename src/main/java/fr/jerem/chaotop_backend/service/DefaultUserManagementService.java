package fr.jerem.chaotop_backend.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import fr.jerem.chaotop_backend.dto.UserProfileResponse;
import fr.jerem.chaotop_backend.exception.EmailAlreadyUsedException;
import fr.jerem.chaotop_backend.exception.InvalidUserDetailsException;
import fr.jerem.chaotop_backend.exception.InvalidUserProfileException;
import fr.jerem.chaotop_backend.exception.UserNotFoundException;
import fr.jerem.chaotop_backend.model.AppUserDetails;
import fr.jerem.chaotop_backend.model.DataBaseEntityUser;
import fr.jerem.chaotop_backend.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementation of {@link UserManagementService} responsible for handling
 * user-related operations.
 * <p>
 * This service provides functionalities for user creation, retrieval, and
 * validation.
 * It interacts with the {@link UserRepository} to persist and fetch user data.
 * It uses the {@link PasswordEncoder} to hash the password before storing in
 * the database.
 * </p>
 */
@Service
@Slf4j
public class DefaultUserManagementService implements UserManagementService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructs a {@code DefaultUserManagementService} with the necessary
     * dependencies.
     *
     * @param userRepository  the repository handling user persistence
     * @param passwordEncoder the encoder for hashing user passwords
     */
    public DefaultUserManagementService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Creates a new user with the given credentials.
     * <p>
     * The password is hashed before being stored in the database.
     * </p>
     *
     * @param email         the email of the new user
     * @param plainPassword the raw password before encoding
     * @param name          the name of the new user
     * @return an {@link AppUserDetails} object representing the newly created user
     */
    @Override
    public AppUserDetails createUser(String email, String plainPassword, String name) {

        if (isEmailAlreadyUsed(email)) {
            throw new EmailAlreadyUsedException("Email '" + email + "' is already present in database.",
                    "DefaultUserManagementService.createUser");
        }

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
    public UserDetails getUserbyEmail(String email) {
        return this.userRepository.findByEmail(email).map((user) -> new AppUserDetails(user))
                .orElseThrow(() -> new UserNotFoundException("User with " + email + " not found",
                        "DefaultUserManagementService.getUserbyEmail"));
    }

    @Override
    public Optional<Long> getUserId(String email) {
        return this.userRepository.findByEmail(email)
                .map(user -> user.getId().longValue());
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
    public UserProfileResponse getUserProfile(String email) {
        UserDetails userDetails = getUserbyEmail(email);

        if (!(userDetails instanceof AppUserDetails)) {
            throw new InvalidUserDetailsException("UserDetails is not of the expected type",
                    "DefaultUserManagementService.getUserProfile");
        }
        AppUserDetails appUserDetails = (AppUserDetails) userDetails;

        return new UserProfileResponse(
                appUserDetails.getId(),
                appUserDetails.getName(),
                appUserDetails.getEmail(),
                appUserDetails.getCreatedAt().toString(),
                appUserDetails.getUpdatedAt().toString());
    }

    @Override
    public boolean isEmailAlreadyUsed(String email) {

        Optional<Long> optionalUserId = getUserId(email);

        return optionalUserId.isPresent();
    }

    @Override
    public Optional<DataBaseEntityUser> getUserEntityById(Long userId) {
        return userRepository.findById(userId.intValue());

    }

    @Override
    public Optional<DataBaseEntityUser> getUserEntityByMail(String email) {
        return userRepository.findByEmail(email);

    }

    @Override
    public UserProfileResponse getUserProfilebyId(Long userId) {

        Optional<DataBaseEntityUser> optionalUserEntity = getUserEntityById(userId);

        // check if empty
        if (optionalUserEntity.isEmpty())
            throw new UserNotFoundException("User with id " + userId + "not found",
                    "DefaultUserManagementService.getUserEntityByMail");

        DataBaseEntityUser user = optionalUserEntity.get();
        // check if email field is set
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            throw new InvalidUserProfileException("User with ID " + userId + " has an invalid email.",
                    "DefaultUserManagementService.getUserProfilebyId");
        }

        return getUserProfile(user.getEmail());
    }

}
