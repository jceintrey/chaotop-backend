package fr.jerem.chaotop_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.jerem.chaotop_backend.model.RentalEntity;
import java.util.Optional;
import java.util.List;


/**
 * Repository interface for managing {@link RentalEntity} persistence.  
 * <p>  
 * Extends {@link JpaRepository} to provide standard CRUD operations  
 * and database interactions for message entities.  
 * </p>  
 *  
 * @see JpaRepository  
 */
@Repository
public interface RentalRepository extends JpaRepository<RentalEntity, Long> {

    Optional<RentalEntity> findById(long id);

}
