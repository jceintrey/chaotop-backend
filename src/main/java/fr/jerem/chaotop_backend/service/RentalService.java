package fr.jerem.chaotop_backend.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;

import org.springframework.stereotype.Service;

import fr.jerem.chaotop_backend.dto.RentalResponse;
import fr.jerem.chaotop_backend.model.DataBaseEntityUser;
import fr.jerem.chaotop_backend.model.RentalEntity;
import fr.jerem.chaotop_backend.repository.RentalRepository;
import fr.jerem.chaotop_backend.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RentalService {

    private RentalRepository rentalRepository;
    private UserRepository userRepository;
    private final ModelMapper modelMapper;

    public RentalService(
            RentalRepository rentalRepository,
            UserRepository userRepository,
            ModelMapper modelMapper) {

        this.rentalRepository = rentalRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    public List<RentalResponse> getAllRentals() {

        List<RentalEntity> rentals = rentalRepository.findAll();
        List<RentalResponse> rentalResponses = new ArrayList<>();

        for (RentalEntity rentalEntity : rentals) {
            rentalResponses.add(modelMapper.map(rentalEntity, RentalResponse.class));
        }
        return rentalResponses;

    }

    public Optional<RentalResponse> getRentalById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }

        return rentalRepository.findById(id)
                .map(rentalEntity -> modelMapper.map(rentalEntity, RentalResponse.class));
    }

    /**
     * Create a new Rental given the arguments and save it to the repository
     * 
     * @param name        the name of the new rental.
     * @param surface     the surface of the new rental.
     * @param price       the price of the new rental.
     * @param picture     the optional picture srting url reference of the new
     *                    rental.
     * @param description the description of the new rental.
     * @param owner       the DataBaseEntityUser owner of the rental.
     * 
     * @return the ID of the new Rental
     */
    public Integer createRental(String name, double surface, BigDecimal price, String picture,
            String description, DataBaseEntityUser owner) {

        if (owner == null || owner.getId() == null) {
            throw new IllegalArgumentException("Invalid owner");
        }
        RentalEntity rentalEntity = new RentalEntity();
        rentalEntity.setName(name);
        rentalEntity.setPrice(price);
        rentalEntity.setPicture(picture);
        rentalEntity.setOwner(owner);
        rentalEntity.setDescription(description);
        rentalEntity.setCreatedAt(LocalDateTime.now());
        rentalEntity.setUpdatedAt(LocalDateTime.now());
        RentalEntity savedRental = rentalRepository.save(rentalEntity);
        return savedRental.getId();
    }

    public DataBaseEntityUser getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

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
    public Optional<RentalResponse> updateRental(Long id, String name, BigDecimal price, double surface,
            String description) {
        Optional<RentalEntity> optionalRentalEntity = rentalRepository.findById(id);

        if (optionalRentalEntity.isPresent()) {
            RentalEntity rentalEntity = optionalRentalEntity.get();

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

            return Optional.of(modelMapper.map(updatedRental, RentalResponse.class));
        }

        return Optional.empty();

    }

}