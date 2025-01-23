package fr.jerem.chaotop_backend.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import fr.jerem.chaotop_backend.model.DBUser;
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
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void createUser(String email, String plainPassword, String name) {
        DBUser user = new DBUser();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(plainPassword));
        user.setName(name);

        userRepository.save(user);
    }
}
