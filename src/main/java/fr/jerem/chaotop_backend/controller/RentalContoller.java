package fr.jerem.chaotop_backend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import fr.jerem.chaotop_backend.dto.RentalCreateResponse;
import fr.jerem.chaotop_backend.dto.RentalListResponse;
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

import java.util.Optional;

import org.modelmapper.ModelMapper;
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
    private final ModelMapper modelMapper;

    public RentalContoller(
            RentalService rentalService,
            UserManagementService userManagementService,
            ModelMapper modelMapper) {
        this.rentalService = rentalService;
        this.modelMapper = modelMapper;
        log.debug("RentalContoller initialized.");
    }

    /**
     * Mapping with Get method used to get the whole rental list
     * 
     * @return {@link }
     * 
     */
    @GetMapping("")
    public ResponseEntity<RentalListResponse> getAllRentals() {
        log.debug("@GetMapping(\"\")");
        List<RentalResponse> rentalResponses = rentalService.getAllRentals();
        RentalListResponse response = new RentalListResponse(rentalResponses);
        log.debug("Retrieved {} rentals", rentalResponses.size());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RentalResponse> getRentalById(@PathVariable("id") final Long id) {
        log.debug("@GetMapping(\"/{id}\")");

        Optional<RentalResponse> rentalResponse = rentalService.getRentalById(id);

        return rentalResponse
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
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
