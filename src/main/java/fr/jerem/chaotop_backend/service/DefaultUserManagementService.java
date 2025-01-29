package fr.jerem.chaotop_backend.service;

import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
    public AppUserDetails getUserbyEmail(String email) {
        DataBaseEntityUser user = this.userRepository.findByEmail(email);
        return new AppUserDetails(user);
    }
    @Override
    public Integer getUserId(String email) {
        return this.userRepository.findByEmail(email).getId();

    }

}
