package fr.jerem.chaotop_backend.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import fr.jerem.chaotop_backend.configuration.AppConfig;
import fr.jerem.chaotop_backend.dto.RentalResponse;
import fr.jerem.chaotop_backend.exception.RentalNotFoundException;
import fr.jerem.chaotop_backend.exception.UserNotFoundException;
import fr.jerem.chaotop_backend.model.DataBaseEntityUser;
import fr.jerem.chaotop_backend.model.RentalEntity;
import fr.jerem.chaotop_backend.repository.RentalRepository;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementation of {@link RentalService} responsible for managing rental
 * entities.
 * <p>
 * This service handles operations like retrieving, creating, and updating
 * rental details.
 * It communicates with the {@link RentalRepository} for persistence and
 * {@link UserManagementService} for user validation.
 * 
 * @see ModelMapper used to convert {@link RentalEntity} to
 *      {@link RentalResponse}
 *      with its specific configuration in {@link AppConfig} configuration file.
 *      </p>
 */
@Service
@Slf4j
public class DefaultRentalService implements RentalService {

    private RentalRepository rentalRepository;
    private UserManagementService userManagementService;
    private final ModelMapper modelMapper;
    private StorageService storageService;

    /**
     * Constructs a {@code DefaultRentalService} with the necessary dependencies.
     * 
     * @param rentalRepository      the repository for managing rental data
     * @param userManagementService the service for managing user-related operations
     * @param modelMapper           the model mapper to convert entities to DTOs
     * 
     *
     * 
     */
    public DefaultRentalService(
            RentalRepository rentalRepository,
            UserManagementService userManagementService,
            ModelMapper modelMapper,
            StorageService storageService) {

        this.rentalRepository = rentalRepository;
        this.userManagementService = userManagementService;
        this.modelMapper = modelMapper;
        this.storageService = storageService;
    }

    /**
     * Retrieves all rentals.
     * <p>
     * This method retrieves all rental entities from the database and maps them to
     * DTOs
     * in order to be used by controller.
     * </p>
     * 
     * @return a list of {@link RentalResponse} objects representing all rentals
     */
    @Override
    public List<RentalResponse> getAllRentals() {

        List<RentalEntity> rentals = rentalRepository.findAll();
        List<RentalResponse> rentalResponses = new ArrayList<>();

        for (RentalEntity rentalEntity : rentals) {
            rentalResponses.add(modelMapper.map(rentalEntity, RentalResponse.class));
        }
        return rentalResponses;

    }

    /**
     * Retrieves a rental by its ID.
     * 
     * <p>
     * This method is intented to be used by controller.
     * </p>
     * 
     * @param id the ID of the rental to retrieve
     * @return an {@link Optional} containing a {@link RentalResponse} if found,
     *         otherwise empty
     * @throws IllegalArgumentException if the provided ID is {@code null}
     */
    @Override
    public RentalResponse getRentalById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        RentalEntity rentalEntity = rentalRepository.findById(id)
                .orElseThrow(() -> new RentalNotFoundException("Rental with id " + id + " not found",
                        "DefaultRentalService.getRentalById"));

        return modelMapper.map(rentalEntity, RentalResponse.class);
    }

    /**
     * Retrieves a rental entity by its ID.
     * <p>
     * This method should only be used by other services.
     * </p>
     * 
     * @param id the ID of the rental entity
     * @return an {@link Optional} containing the {@link RentalEntity} if found
     */
    @Override
    public Optional<RentalEntity> getRentalEntityById(Long id) {
        return rentalRepository.findById(id);
    }

    @Override
    public Integer createRental(String name, double surface, BigDecimal price, MultipartFile picture,
            String description, String usermail) {

        Optional<DataBaseEntityUser> optionalDataBaseEntityUser = userManagementService.getUserEntityByMail(usermail);
        if (optionalDataBaseEntityUser.isEmpty())
            throw new UserNotFoundException("User with id " + usermail + "not found",
                    "DefaultRentalService.createRental");

        DataBaseEntityUser datatBaseEntityUser = optionalDataBaseEntityUser.get();

        String pictureUrl = storageService.uploadImage(picture);

        RentalEntity rentalEntity = new RentalEntity();
        rentalEntity.setName(name);
        rentalEntity.setSurface(surface);
        rentalEntity.setPrice(price);
        rentalEntity.setPicture(pictureUrl);
        rentalEntity.setDescription(description);
        rentalEntity.setOwner(datatBaseEntityUser);
        rentalEntity.setCreatedAt(LocalDateTime.now());
        rentalEntity.setUpdatedAt(LocalDateTime.now());

        RentalEntity savedRentalEntity = rentalRepository.save(rentalEntity);

        return savedRentalEntity.getId();

    }

    @Override
    public RentalResponse updateRental(Long id, String name, BigDecimal price, double surface, String description) {

        RentalEntity rentalEntity = rentalRepository.findById(id)
                .orElseThrow(() -> new RentalNotFoundException("Rental with ID: " + id + "not found",
                        "DefaultRentalService.updateRental"));

        if (name != null)
            rentalEntity.setName(name);
        if (price != null)
            rentalEntity.setPrice(price);
        if (surface > 0)
            rentalEntity.setSurface(surface);

        if (description != null)
            rentalEntity.setDescription(description);

        rentalEntity.setUpdatedAt(LocalDateTime.now());

        RentalEntity updatedRental = rentalRepository.save(rentalEntity);

        return modelMapper.map(updatedRental, RentalResponse.class);

    }

}