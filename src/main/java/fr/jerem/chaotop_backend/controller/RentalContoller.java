package fr.jerem.chaotop_backend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import fr.jerem.chaotop_backend.dto.RentalCreateResponse;
import fr.jerem.chaotop_backend.dto.RentalResponse;
import fr.jerem.chaotop_backend.model.DataBaseEntityUser;
import fr.jerem.chaotop_backend.model.RentalEntity;
import fr.jerem.chaotop_backend.service.RentalService;
import fr.jerem.chaotop_backend.service.UserManagementService;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/rentals")
@Slf4j
public class RentalContoller {
    private final RentalService rentalService;

    public RentalContoller(
            RentalService rentalService,
            UserManagementService userManagementService) {
        this.rentalService = rentalService;

        log.debug("RentalContoller initialized.");
    }

    /**
     * Mapping with Get method used to get the whole rental list
     * 
     * @return {@link }
     * 
     */
    @GetMapping("")
    public ResponseEntity<List<RentalResponse>> getAllRentals() {
        log.debug(" @GetMapping(\"/\")");
        List<RentalEntity> rentals = this.rentalService.getAllRentals();
        log.debug("Retrieved {} rentals", rentals.size());
        List<RentalResponse> rentalResponses = new ArrayList<>();
        for (RentalEntity rental : rentals) {
            rentalResponses.add(mapToRentalResponse(rental));
        }

        log.debug(rentalResponses.toString());
        return ResponseEntity.ok(rentalResponses);
    }

    private RentalResponse mapToRentalResponse(RentalEntity rental) {
        RentalResponse rentalResponse = new RentalResponse();
        rentalResponse.setId(rental.getId());
        rentalResponse.setName(rental.getName());
        rentalResponse.setSurface(rental.getSurface());
        rentalResponse.setPrice(rental.getPrice());
        rentalResponse.setPicture(rental.getPicture());
        rentalResponse.setDescription(rental.getDescription());
        rentalResponse.setOwner(rental.getOwner().getId());
        rentalResponse.setCreatedAt(rental.getCreatedAt());
        rentalResponse.setUpdatedAt(rental.getUpdatedAt());
        return rentalResponse;
    }

    @GetMapping("/{id}")
    public ResponseEntity<RentalResponse> getRentalById(@PathVariable("id") final Long id) {
        log.debug("@GetMapping(\"/{id}\")");

        RentalResponse rentalResponse = rentalService.getRentalById(id)
                .map(rental -> {
                    RentalResponse response = new RentalResponse();
                    response.setId(rental.getId());
                    response.setName(rental.getName());
                    response.setSurface(rental.getSurface());
                    response.setPrice(rental.getPrice());
                    response.setPicture(rental.getPicture());
                    response.setDescription(rental.getDescription());
                    response.setOwner(rental.getOwner().getId());
                    response.setCreatedAt(rental.getCreatedAt());
                    response.setUpdatedAt(rental.getUpdatedAt());
                    return response;
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Rental not found"));

        return ResponseEntity.ok(rentalResponse);
    }

    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<RentalCreateResponse> createRental(
            @RequestParam("name") String name,
            @RequestParam("surface") double surface,
            @RequestParam("price") BigDecimal price,
            @RequestParam(value = "picture", required = false) MultipartFile picture,
            @RequestParam(value = "description", required = false) String description) {

        log.debug("@PostMapping(\"\")");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        DataBaseEntityUser user = rentalService.getUserByEmail(userEmail);

        if (user == null) {
            return ResponseEntity.badRequest().body(new RentalCreateResponse("User not found"));
        }
        try {
            // TODO a specific service to store image

            Integer rentalId = rentalService.createRental(name, surface, price, null, description, user);

            URI location = URI.create("/api/rentals/" + rentalId);
            return ResponseEntity.created(location).body(new RentalCreateResponse("Rental created!"));

        } catch (Exception e) {
            return ResponseEntity.status(500).body(new RentalCreateResponse("Error: " + e.getMessage()));
        }
    }

    @PutMapping("path/{id}")
    public String putMethodName(@PathVariable String id, @RequestBody String entity) {
        // TODO: process PUT request

        return entity;
    }

}
