package fr.jerem.chaotop_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.jerem.chaotop_backend.model.RentalEntity;
import java.util.Optional;

/**
 * Repository interface for managing {@link RentalEntity} entities.
 * 
 * <p>
 * This interface provides basic CRUD operations through JpaRepository
 * and defines a custom query method to find rentals by their id
 * </p>
 * 
 * 
 */
@Repository
public interface RentalRepository extends JpaRepository<RentalEntity, Long> {

    Optional<RentalEntity> findById(long id);
}
