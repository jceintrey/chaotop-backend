package fr.jerem.chaotop_backend.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import fr.jerem.chaotop_backend.dto.RentalResponse;
import fr.jerem.chaotop_backend.model.RentalEntity;

/**
 * Service interface for managing rentals like retrieving all rentals, or a
 * specific one, creating or updating.
 * <p>
 * This service defines the contract for managing rentals.
 * 
 * </p>
 * 
 * @see DefaultMessageService for the default implementation.
 */
public interface RentalService {

   /**
    * Retrieves a list of all available rentals.
    * 
    * @return a list of {@link RentalResponse} containing rental details.
    */
   List<RentalResponse> getAllRentals();

   /**
    * Retrieves a rental by its unique identifier.
    * This method is intended for use in controllers to return a DTO representation
    * of the rental.
    * 
    * @param id the unique identifier of the rental.
    * @return an {@link Optional} containing the {@link RentalResponse} if found,
    *         otherwise empty.
    */
   Optional<RentalResponse> getRentalById(Long id);

   /**
    * Retrieves a rental entity by its unique identifier.
    * <p>
    * <b>Note:</b> This method is intended for internal service use only and should
    * not be used in controllers.
    * Controllers should use {@link #getRentalById(Long)} to ensure DTO
    * encapsulation.
    * </p>
    * 
    * @param id the unique identifier of the rental.
    * @return an {@link Optional} containing the {@link RentalEntity} if found,
    *         otherwise empty.
    */
   Optional<RentalEntity> getRentalEntityById(Long id);

   /**
    * Create a new Rental and save it to the repository
    * 
    * @param name        the name of the new rental.
    * @param surface     the surface of the new rental.
    * @param price       the price of the new rental.
    * @param picture     the optional picture srting url reference of the new
    *                    rental.
    * @param description the description of the new rental.
    * @param userId      the userId of the rental's owner
    * 
    * @return the ID of the new Rental
    */
   Optional<Integer> createRental(String name, double surface, BigDecimal price, MultipartFile picture,
         String description, Long userId);

   /**
    * Updates a rental entity if exist with args values.
    * 
    * 
    * @param id          the ID of the rental to update.
    * @param name        the new name of the rental if not null.
    * @param price       the new price of the rental if not null.
    * @param surface     the new surface of the rental if > 0.
    * @param description the new description if not null.
    * @return a {@link Optional} containing the {@link RentalResponse} wrapped from
    *         the new {@link RentalEntity} or null if not present.
    */
   Optional<RentalResponse> updateRental(Long id, String name, BigDecimal price, double surface,
         String description);

}