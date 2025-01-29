package fr.jerem.chaotop_backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.jerem.chaotop_backend.dto.LoginRequest;
import fr.jerem.chaotop_backend.dto.MeResponse;
import fr.jerem.chaotop_backend.dto.RegisterRequest;
import fr.jerem.chaotop_backend.dto.TokenResponse;
import fr.jerem.chaotop_backend.model.AppUserDetails;
import fr.jerem.chaotop_backend.service.JwtFactory;
import fr.jerem.chaotop_backend.service.UserManagementService;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtFactory jwtFactory;
    private UserManagementService userManagementService;

    public AuthController(
            AuthenticationManager authenticationManager,
            JwtFactory jwtFactory,
            UserManagementService userManagementService) {
        this.authenticationManager = authenticationManager;
        this.jwtFactory = jwtFactory;
        this.userManagementService = userManagementService;
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
        try {
            log.debug("@PostMapping(\"/login\") - LoginRequest: {}", request);

            // Authenticate with user credentials
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

            // Generate token
            String token = jwtFactory.createToken(authentication);

            // Build and return the Token response
            TokenResponse response = new TokenResponse();
            response.setToken(token);
            log.debug("@PostMapping(\"/login\") - success for: {}", request.getEmail());
            return ResponseEntity.ok(response);

        } catch (AuthenticationException e) {
            log.error("@PostMapping(\"/login\") error during user authentication \n" + e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new TokenResponse());
        } catch (Exception e) {
            log.error("An exeption has occured " + e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new TokenResponse());
        }
    }

    /**
     * Mapping with Get method used to check the api connectivity
     * 
     * @return {@link ResponseEntity<Map<String, String>>}
     * 
     */
    @GetMapping("/me")
    public ResponseEntity<MeResponse> getUserInformations() {
        log.debug("@GetMapping(\"/me\")");
        // Get authentication context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        

        try {
            // Get a UserDetails from userManagementService
            AppUserDetails userDetails = (AppUserDetails) this.userManagementService
                    .getUserbyEmail(authentication.getName());

            log.debug(authentication.getCredentials().toString());
            // Build the response
            MeResponse meResponse = new MeResponse(
                    userDetails.getName(),
                    userDetails.getEmail(),
                    userDetails.getCreatedAt().toString(),
                    userDetails.getUpdatedAt().toString());

            log.debug("Return response: {}", meResponse.toString());
            return ResponseEntity.ok(meResponse);
        } catch (Exception e) {
            log.error("Error fetching user details: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MeResponse("", "", "", ""));
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
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        try {
            log.debug("@PostMapping(\"/register\") - RegisterRequest: {}", request);

            // Create user with userDetails implementation
            UserDetails userDetails = this.userManagementService.createUser(
                    request.getEmail(),
                    request.getPassword(),
                    request.getName());

            log.debug(userDetails.toString());

            return ResponseEntity.ok("{}");
        } catch (AuthenticationException e) {
            log.error("@PostMapping(\"/login\") error during user authentication \n" + e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("{}");

        } catch (Exception e) {
            log.error("An exeption has occured " + e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("{}");
        }
    }

}