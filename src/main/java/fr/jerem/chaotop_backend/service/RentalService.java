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
}