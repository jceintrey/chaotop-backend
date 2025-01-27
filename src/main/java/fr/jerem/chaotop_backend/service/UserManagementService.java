package fr.jerem.chaotop_backend.service;

import org.springframework.security.core.userdetails.UserDetails;

/**
 * Interface for managing users
 * This service provides methods for user creation or details
 */
public interface UserManagementService {
    /**
     * Creates a new user with the given details and returns a UserDetails object.
     * This method is responsible for creating a new user in the application.
     * 
     * @param email         the email address of the new user.
     * @param plainPassword the plain-text password of the new user
     * @param name          the name of the new user.
     * @return a UserDetails object that represents the created user.
     */
    public UserDetails createUser(String email, String plainPassword, String name);

    public UserDetails getUserbyEmail(String email);
}
