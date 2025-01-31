package fr.jerem.chaotop_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.jerem.chaotop_backend.model.DataBaseEntityUser;

/**
 * Repository interface for managing {@link DataBaseEntityUser} persistence.  
 * <p>  
 * Extends {@link JpaRepository} to provide standard CRUD operations  
 * and database interactions for message entities.  
 * </p>  
 *  
 * @see JpaRepository  
 */
@Repository
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
