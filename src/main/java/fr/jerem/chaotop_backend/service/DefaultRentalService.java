package fr.jerem.chaotop_backend.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import fr.jerem.chaotop_backend.dto.RentalResponse;
import fr.jerem.chaotop_backend.model.DataBaseEntityUser;
import fr.jerem.chaotop_backend.model.RentalEntity;
import fr.jerem.chaotop_backend.repository.RentalRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DefaultRentalService implements RentalService {

    private RentalRepository rentalRepository;
    private UserManagementService userManagementService;
    private final ModelMapper modelMapper;

    public DefaultRentalService(
            RentalRepository rentalRepository,
            UserManagementService userManagementService,
            ModelMapper modelMapper) {

        this.rentalRepository = rentalRepository;
        this.userManagementService = userManagementService;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<RentalResponse> getAllRentals() {

        List<RentalEntity> rentals = rentalRepository.findAll();
        List<RentalResponse> rentalResponses = new ArrayList<>();

        for (RentalEntity rentalEntity : rentals) {
            rentalResponses.add(modelMapper.map(rentalEntity, RentalResponse.class));
        }
        return rentalResponses;

    }

    @Override
    public Optional<RentalResponse> getRentalById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }

        return rentalRepository.findById(id)
                .map(rentalEntity -> modelMapper.map(rentalEntity, RentalResponse.class));
    }

    @Override
    public Optional<RentalEntity> getRentalEntityById(Long id) {
        return rentalRepository.findById(id);
    }

    @Override
    public Integer createRental(String name, double surface, BigDecimal price, String picture,
            String description, Long userId) {
        if (userId == null)
            throw new IllegalArgumentException("Invalid userId");

        DataBaseEntityUser datatBaseEntityUser = userManagementService.getUserById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + userId));

        RentalEntity rentalEntity = new RentalEntity();
        rentalEntity.setName(name);
        rentalEntity.setSurface(surface);
        rentalEntity.setPrice(price);
        rentalEntity.setPicture(picture);
        rentalEntity.setDescription(description);
        rentalEntity.setOwner(datatBaseEntityUser);
        rentalEntity.setCreatedAt(LocalDateTime.now());
        rentalEntity.setUpdatedAt(LocalDateTime.now());

        rentalRepository.save(rentalEntity);

        return 0;

    }

    @Override
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