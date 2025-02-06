package fr.jerem.chaotop_backend.service;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;

import fr.jerem.chaotop_backend.dto.UserProfileResponse;
import fr.jerem.chaotop_backend.model.UserEntity;

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
    public UserDetails createUser(String email, String plainPassword, String name);

    /**
     * Retrieves a {@link UserDetails} object by email.
     * This object should be cast to its implementation.
     * 
     * @param {@link String} the email.
     * @return a {@link UserDetails}
     */
    public UserDetails getUserbyEmail(String email);

    /**
     * Retrieves the user uniq identifier by their email.
     * 
     * @param {@link String} the email.
     * @return an {@link Optional} containing the id if found,
     *         otherwise empty.
     */
    public Optional<Long> getUserId(String email);

    /**
     * Retrieves the user detailed informations by their email.
     * 
     * This method is intented to be used by the controller as it can easily
     * return the {@link UserProfileResponse}
     * 
     * @param {@link String} email of the user
     * @return an {@link Optional} containing the {@link UserDetails} if found,
     *         otherwise empty.
     */
    public UserProfileResponse getUserProfilebyEmail(String email);

    /**
     * Retrieves a UserProfileResponse by their Id.
     * 
     * This method is intented to be used by the controller as it can easily
     * return the {@link UserProfileResponse}
     * 
     * @param {@link Long} the userId.
     * @return the {@link UserProfileResponse}
     */
    public UserProfileResponse getUserProfilebyId(Long userId);

    /**
     * Return true if the user is already present in the Database.
     * 
     * @param {@link String} email of the user
     * @return a {@link boolean} true if the user email already exist, and false
     *         otherwise
     */
    public boolean isEmailAlreadyUsed(String email);

    /**
     * Retrieves the {@link UserEntity} by their Id.
     * 
     * This method is intented to be used by other services and not
     * by controllers themselves in order to respect the layer model.
     * 
     * @param {@link Long} id of the user
     * @return an {@link Optional} containing the {@link UserEntity} if
     *         found,
     *         otherwise empty.
     */
    public Optional<UserEntity> getUserEntityById(Long userId);

    /**
     * Retrieves the {@link UserEntity} by their Email.
     * 
     * This method is intented to be used by other services and not
     * by controllers themselves in order to respect the layer model.
     * 
     * @param {@link String} email of the user
     * @return an {@link Optional} containing the {@link UserEntity} if
     *         found,
     *         otherwise empty.
     */
    public Optional<UserEntity> getUserEntityByMail(String email);
}
