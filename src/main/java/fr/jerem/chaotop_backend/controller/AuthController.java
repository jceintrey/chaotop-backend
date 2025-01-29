package fr.jerem.chaotop_backend.controller;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;



import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.jerem.chaotop_backend.dto.LoginRequest;
import fr.jerem.chaotop_backend.dto.UserProfileResponse;
import fr.jerem.chaotop_backend.dto.RegisterRequest;
import fr.jerem.chaotop_backend.dto.TokenResponse;
import fr.jerem.chaotop_backend.service.AuthenticationService;
import fr.jerem.chaotop_backend.service.UserManagementService;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {

    private UserManagementService userManagementService;
    private final AuthenticationService authenticationService;

    public AuthController(
            AuthenticationService authenticationService,
            UserManagementService userManagementService) {
        this.userManagementService = userManagementService;
        this.authenticationService = authenticationService;
        log.debug("AuthController initialized.");

    }

    /**
     * Mapping with POST method used to request a token to consum the api
     * 
     * <p>
     * This method first authenticate the Post request mapped to the DTO
     * LoginRequest
     * and then authenticate the user credentials.
     * If authentication is ok, generate and return a token in the DTO TokenResponse
     * 
     * @return {@link ResponseEntity<TokenResponse>}
     */
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest request) {
        log.debug("@PostMapping(\"/login\")");
        try {
            TokenResponse tokenResponse = authenticationService.authenticate(request);
            return ResponseEntity.ok(tokenResponse);
        } catch (Exception e) {
            log.error("Authentication failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new TokenResponse("error"));
        }
    }

    /**
     * Mapping with Get method used to check the api connectivity
     * 
     * @return {@link ResponseEntity<Map<String, String>>}
     * 
     */
    @GetMapping("/me")
    public ResponseEntity<UserProfileResponse> getUserInformations() {
        log.debug("@GetMapping(\"/me\")");


        Optional<String> optionalAuthenticatedUserEmail = authenticationService.getAuthenticatedUserEmail();

        
        if (optionalAuthenticatedUserEmail.isEmpty()) {
            log.error("No authenticated user found.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new UserProfileResponse("", "", "", ""));
        }
    
        String email = optionalAuthenticatedUserEmail.get();
        try {
            UserProfileResponse meResponse = userManagementService.getUserInformationResponse(email);

            if (meResponse.getEmail().isEmpty()) {
                log.error("Error fetching user details for email: {}", email);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new UserProfileResponse("", "", "", ""));
            }

            log.debug("Return response: {}", meResponse.toString());
            return ResponseEntity.ok(meResponse);

        } catch (Exception e) {
            log.error("Unexpected error: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new UserProfileResponse("", "", "", ""));
        }
    }

    /**
     * Mapping with POST method used to register a user to the application
     *
     * <p>
     *
     * @return {@link ResponseEntity<TokenResponse>}
     */
    @PostMapping("/register")
    public ResponseEntity<TokenResponse> register(@RequestBody RegisterRequest request) {
        log.debug("@PostMapping(\"/register\") - RegisterRequest: {}", request);

        try {
            // Check if the email is already taken
            if (userManagementService.isEmailAlreadyUsed(request.getEmail())) {
                log.error("Email {} is already used.", request.getEmail());
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new TokenResponse(""));
            }
            // Create user with userDetails implementation
            userManagementService.createUser(
                    request.getEmail(),
                    request.getPassword(),
                    request.getName());

            // Authenticate if user is created
            LoginRequest loginRequest = new LoginRequest(request.getEmail(), request.getPassword());
            TokenResponse tokenResponse = authenticationService.authenticate(loginRequest);

            log.debug("User created successfully: {}", request.getEmail());

            return ResponseEntity.ok(tokenResponse);
        } catch (Exception e) {
            log.error("Error occurred during user registration: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new TokenResponse(""));
        }
    }

}