package fr.jerem.chaotop_backend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import fr.jerem.chaotop_backend.dto.RentalCreateResponse;
import fr.jerem.chaotop_backend.dto.RentalListResponse;
import fr.jerem.chaotop_backend.dto.RentalResponse;

import fr.jerem.chaotop_backend.service.AuthenticationService;
import fr.jerem.chaotop_backend.service.RentalService;
import fr.jerem.chaotop_backend.service.UserManagementService;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/rentals")
@Slf4j
public class RentalContoller {
    private final RentalService rentalService;
    private final AuthenticationService authenticationService;
    private final UserManagementService userManagementService;

    public RentalContoller(
            RentalService rentalService,
            UserManagementService userManagementService,
            AuthenticationService authenticationService) {
        this.rentalService = rentalService;
        this.userManagementService = userManagementService;
        this.authenticationService = authenticationService;

        log.debug("RentalContoller initialized.");
    }

    /**
     * Retrieves all rentals.
     * 
     * @return {@link ResponseEntity} containing the {@link RentalListResponse}
     *         representing a list of {@link RentalResponse}
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

    /**
     * Retrieves a rental by its ID.
     * 
     * @param id The ID of the rental to retrieve.
     * @return {@link ResponseEntity} containing the {@link RentalResponse} if
     *         found, otherwise a 404 response.
     */
    @GetMapping("/{id}")
    public ResponseEntity<RentalResponse> getRentalById(@PathVariable("id") final Long id) {
        log.debug("@GetMapping(\"/{id}\")");

        Optional<RentalResponse> rentalResponse = rentalService.getRentalById(id);

        return rentalResponse
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Creates a new rental.
     * 
     * @param name        The name of the rental.
     * @param surface     The surface area of the rental.
     * @param price       The price of the rental.
     * @param picture     Optional picture of the rental.
     * @param description Optional description of the rental.
     * @return {@link ResponseEntity} containing the creation status and rental ID
     *         if successful.
     */
    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<RentalCreateResponse> createRental(
            @RequestParam("name") String name,
            @RequestParam("surface") double surface,
            @RequestParam("price") BigDecimal price,
            @RequestParam(value = "picture", required = false) MultipartFile picture,
            @RequestParam(value = "description", required = false) String description) {

        log.debug("@PostMapping(\"\")");

        Optional<String> optionalAuthenticatedUserEmail = authenticationService.getAuthenticatedUserEmail();

        if (optionalAuthenticatedUserEmail.isEmpty()) {
            log.error("No authenticated user found.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String email = optionalAuthenticatedUserEmail.get();
        Optional<Long> optionalUserId = userManagementService.getUserId(email);
        if (optionalUserId.isEmpty()) {
            log.error("No Id found for email {}", email);
            return ResponseEntity.internalServerError().build();
        }

        Long userId = optionalUserId.get();
        try {
            // TODO a specific service to store image

            Integer rentalId = rentalService.createRental(name, surface, price, null, description, userId);

            URI location = URI.create("/api/rentals/" + rentalId);
            return ResponseEntity.created(location).body(new RentalCreateResponse("Rental created!"));

        } catch (Exception e) {
            return ResponseEntity.status(500).body(new RentalCreateResponse("Error: " + e.getMessage()));
        }
    }

    /**
     * Updates an existing rental.
     * 
     * @param id          The ID of the rental to update.
     * @param name        The updated name of the rental.
     * @param surface     The updated surface area of the rental.
     * @param price       The updated price of the rental.
     * @param description The updated description of the rental.
     * @return ResponseEntity containing the updated RentalResponse if found,
     *         otherwise a 404 response.
     */
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<RentalResponse> updateRental(
            @PathVariable("id") final Long id,
            @RequestParam("name") String name,
            @RequestParam("surface") double surface,
            @RequestParam("price") BigDecimal price,
            @RequestParam(value = "description", required = false) String description) {
        log.debug("@PutMapping(\"/{id}\")");

        return rentalService.updateRental(id, name, price, surface, description)
                .map((r) -> ResponseEntity.ok(r))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}
