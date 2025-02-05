package fr.jerem.chaotop_backend.controller;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.jerem.chaotop_backend.dto.UserProfileResponse;

import fr.jerem.chaotop_backend.service.AuthenticationService;
import fr.jerem.chaotop_backend.service.UserManagementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * Controller class responsible for handling user-related operations.
 * <p>
 * - {@link AuthenticationService} service that process the user authentication
 * - {@link UserManagementService} service used for business operations on users
 * </p>
 * 
 */
@RestController
@RequestMapping("/api/user")
@Slf4j
@Tag(name = "UserController", description = "Process business operations on users")
public class UserController {

    private UserManagementService userManagementService;

    public UserController(
            UserManagementService userManagementService) {
        this.userManagementService = userManagementService;
        log.debug("UserController initialized.");

    }

    /**
     * Get user informations.
     * 
     * <p>
     * Use the {@link AuthenticationService} to retrieve a user by their id.
     * 
     * </p>
     * 
     * @return {@link UserProfileResponse} the response DTO.
     * 
     */
    @SecurityRequirement(name = "Bearer_Authentication")
    @Operation(summary = "Get user informations", description = "This endpoint allows to retrieve user details by their Id.", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully get the user details"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
    })

    @GetMapping("/{id}")
    public ResponseEntity<UserProfileResponse> getUserById(@PathVariable("id") final Long id) {
        log.debug("getUserById");

        try {
            Optional<UserProfileResponse> optionalUserProfileResponse = userManagementService.getUserProfilebyId(id);

            if (optionalUserProfileResponse.isPresent()) {
                return ResponseEntity.ok(optionalUserProfileResponse.get());
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new UserProfileResponse(null, "", "", "", ""));
            }

        } catch (Exception e) {
            log.error("Unexpected error: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new UserProfileResponse(null, "", "", "", ""));
        }
    }

}
