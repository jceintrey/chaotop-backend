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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

/**
 * Controller class responsible for handling rental-related operations.
 * <p>
 * This class provides endpoints for creating, updating and retrieving rentals.
 * </p>
 * <p>
 * - {@link RentalService} service that manage rentals.
 * - {@link AuthenticationService} service that handles authentications.
 * - {@link UserManagementService} service that manage users.
 * </p>
 * 
 */
@Tag(name = "RentalContoller", description = "Manage rental-related operations.")
@RestController
@RequestMapping("/api/rentals")
@Slf4j
public class RentalContoller {
    private final RentalService rentalService;
    private final AuthenticationService authenticationService;

    public RentalContoller(
            RentalService rentalService,
            AuthenticationService authenticationService) {
        this.rentalService = rentalService;
        this.authenticationService = authenticationService;

        log.debug("RentalContoller initialized.");
    }

    /**
     * Retrieve all rentals.
     * 
     * <p>
     * Use {@link RentalService} to get all rentals.
     * </p>
     * 
     * @return {@link RentalListResponse} the response DTO.
     */
    @SecurityRequirement(name = "Bearer_Authentication")
    @Operation(summary = "Retrieve all rentals.", description = "This endpoint allows a user to retrieve all rentals.", responses = {
            @ApiResponse(responseCode = "200", description = "Successful, returns the list of rentals"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),

    })
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
     * 
     * @return {@link RentalResponse} the response DTO.
     */
    @SecurityRequirement(name = "Bearer_Authentication")
    @Operation(summary = "Retrieve a rental", description = "This endpoint allows a user to retrieve a rental by providing its ID.", responses = {
            @ApiResponse(responseCode = "200", description = "Successful, returns the rental"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Rental not found"),

    })
    @GetMapping("/{id}")
    public ResponseEntity<RentalResponse> getRentalById(@PathVariable("id") final Long id) {
        log.debug("@GetMapping(\"/{id}\")");

        RentalResponse rentalResponse = rentalService.getRentalById(id);
        return ResponseEntity.ok().body(rentalResponse);

    }

    /**
     * Creates a new rental.
     * 
     * <p>
     * Use {@link AuthenticationService} to retrieve the authenticated user.
     * Use {@link rentalService} to create the new rental.
     * </p>
     * 
     * @param name        The name of the rental.
     * @param surface     The surface area of the rental.
     * @param price       The price of the rental.
     * @param picture     Optional picture of the rental.
     * @param description Optional description of the rental.
     * @return {@link ResponseEntity} containing the creation status and rental ID
     *         if successful.
     */
    @SecurityRequirement(name = "Bearer_Authentication")
    @Operation(summary = "Creates a new rental", description = "This endpoint allows a user to create a new rental in the application.", responses = {
            @ApiResponse(responseCode = "201", description = "Successful, rental successfully created"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),

    })
    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<RentalCreateResponse> createRental(
            @RequestParam("name") String name,
            @RequestParam("surface") double surface,
            @RequestParam("price") BigDecimal price,
            @RequestParam(value = "picture", required = false) MultipartFile picture,
            @RequestParam(value = "description", required = false) String description) {

        // Retrieve the authenticated user email
        String email = authenticationService.getAuthenticatedUserEmail();

        Integer rentalId = rentalService.createRental(name, surface, price, picture, description, email);

        URI location = URI.create("/api/rentals/" + rentalId);
        return ResponseEntity.created(location).body(new RentalCreateResponse("Rental created!"));

    }

    /**
     * Updates an existing rental.
     * 
     * @param id          The ID of the rental to update.
     * @param name        The updated name of the rental.
     * @param surface     The updated surface area of the rental.
     * @param price       The updated price of the rental.
     * @param description The updated description of the rental.
     * @return {@link RentalResponse} the update rental.
     */
    @SecurityRequirement(name = "Bearer_Authentication")
    @Operation(summary = " Updates an existing rental", description = "This endpoint allows a user to update a rental.", responses = {
            @ApiResponse(responseCode = "200", description = "Successful, rental successfully updated"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Not found")

    })
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<RentalResponse> updateRental(
            @PathVariable("id") final Long id,
            @RequestParam("name") String name,
            @RequestParam("surface") double surface,
            @RequestParam("price") BigDecimal price,
            @RequestParam(value = "description", required = false) String description) {

        log.debug("@PutMapping(\"/{id}\")");
        RentalResponse updatedRental = rentalService.updateRental(id, name, price, surface, description);
        return ResponseEntity.ok().body(updatedRental);

    }

}
