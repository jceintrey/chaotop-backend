package fr.jerem.chaotop_backend.service;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;

import fr.jerem.chaotop_backend.dto.UserProfileResponse;
import fr.jerem.chaotop_backend.model.DataBaseEntityUser;

/**
 * Service interface for managing users.
 * It defines methods for create or retrieve informations.
 * <p>
 * This service defines the contract for managing rentals.
 * 
 * </p>
 * 
 * @see DefaultMessageService for the default implementation.
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
    public UserDetails createUser(String email, String plainPassword, String name) throws Exception;

    /**
     * Retrieves a {@link UserDetails} object by email.
     * This object should be cast to its implementation.
     * 
     * @param {@link String} the email.
     * @return an {@link Optional} containing the {@link UserDetails} if found,
     *         otherwise empty.
     */
    public Optional<UserDetails> getUserbyEmail(String email);

    /**
     * Retrieves the user uniq identifier by email.
     * 
     * @param {@link String} the email.
     * @return an {@link Optional} containing the {@link UserDetails} if found,
     *         otherwise empty.
     */
    public Optional<Long> getUserId(String email);

    /**
     * Retrieves the user detailed informations.
     * This method is intented to be used by the controller as it can easily
     * return the {@link UserProfileResponse}
     * 
     * @param {@link String} email of the user
     * @return an {@link Optional} containing the {@link UserDetails} if found,
     *         otherwise empty.
     */
    public UserProfileResponse getUserInformationResponse(String email);

    /**
     * Return true if the user is already present in the Database.
     * 
     * @param {@link String} email of the user
     * @return a {@link boolean} true if the user email already exist, and false
     *         otherwise
     */
    public boolean isEmailAlreadyUsed(String email);

    /**
     * Retrieves the {@link DataBaseEntityUser} by their Id.
     * 
     * This method is intented to be used by other services and not
     * by controllers themselves in order to respect the layer model.
     * 
     * @param {@link String} email of the user
     * @return an {@link Optional} containing the {@link DataBaseEntityUser} if
     *         found,
     *         otherwise empty.
     */
    public Optional<DataBaseEntityUser> getUserById(Long userId);

}
