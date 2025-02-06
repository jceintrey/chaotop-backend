package fr.jerem.chaotop_backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.jerem.chaotop_backend.model.UserEntity;

/**
 * Repository interface for managing {@link UserEntity} persistence.  
 * <p>  
 * Extends {@link JpaRepository} to provide standard CRUD operations  
 * and database interactions for message entities.  
 * </p>  
 *  
 * @see JpaRepository  
 */
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    /**
     * Retrieves a user by the email address.
     * 
     * @param email the email address of the user
     * @return the {@link UserEntity} associated with the given email address,
     *         or {@code null} if no user is found
     */
    Optional<UserEntity> findByEmail(String email);
}
