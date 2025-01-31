package fr.jerem.chaotop_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.jerem.chaotop_backend.model.MessageEntity;


/**
 * Repository interface for managing {@link MessageEntity} persistence.  
 * <p>  
 * Extends {@link JpaRepository} to provide standard CRUD operations  
 * and database interactions for message entities.  
 * </p>  
 *  
 * @see JpaRepository  
 */
@Repository
public interface MessageRepository extends JpaRepository<MessageEntity, Long> { 

}
