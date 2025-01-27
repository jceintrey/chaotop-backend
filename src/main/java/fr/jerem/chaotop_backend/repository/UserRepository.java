package fr.jerem.chaotop_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.jerem.chaotop_backend.model.DataBaseEntityUser;


/**
 * Repository interface for managing {@link DataBaseEntityUser} entities.
 * 
 * <p>This interface provides basic CRUD operations through JpaRepository
 * and defines a custom query method to find users by their email address.</p>
 * 
 * 
 */
public interface UserRepository extends JpaRepository<DataBaseEntityUser, Integer> {
    /**
     * Retrieves a user by the email address.
     * 
     * @param email the email address of the user
     * @return the {@link DataBaseEntityUser} associated with the given email address,
     *         or {@code null} if no user is found
     */
    public DataBaseEntityUser findByEmail(String email);
}
