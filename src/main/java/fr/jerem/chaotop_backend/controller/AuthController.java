package fr.jerem.chaotop_backend.controller;

import java.util.Collections;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.jerem.chaotop_backend.dto.LoginRequest;
import fr.jerem.chaotop_backend.dto.MeResponse;
import fr.jerem.chaotop_backend.dto.TokenResponse;
import fr.jerem.chaotop_backend.model.CustomUserDetails;
import fr.jerem.chaotop_backend.service.CustomUserDetailsService;
import fr.jerem.chaotop_backend.service.JWTService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private AuthenticationManager authenticationManager;
    private JWTService jwtService;

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    public AuthController(AuthenticationManager authenticationManager, JWTService jwtService,
            CustomUserDetailsService customUserDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        logger.debug("AuthController initialized.");

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
            logger.debug("@PostMapping(\"/login\") - LoginRequest: {}", request);

            // Authenticate with user credentials
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

            // Generate token
            String token = jwtService.generateToken(authentication);

            // Build and return the Token response
            TokenResponse response = new TokenResponse();
            response.setToken(token);
            logger.debug("@PostMapping(\"/login\") - success for: {}", request.getEmail());
            return ResponseEntity.ok(response);

        } catch (AuthenticationException e) {
            logger.error("@PostMapping(\"/login\") error during user authentication \n" + e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new TokenResponse());
        } catch (Exception e) {
            logger.error("An exeption has occured " + e);
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
    @GetMapping("/status")
    public ResponseEntity<Map<String, String>> getStatus() {
        logger.debug("@GetMapping(\"/status\")");
        return ResponseEntity.ok(Collections.singletonMap("apistatus", "ok"));
    }

    /**
     * Mapping with Get method used to check the api connectivity
     * 
     * @return {@link ResponseEntity<Map<String, String>>}
     * 
     */
    @GetMapping("/me")
    public ResponseEntity<MeResponse> getUserInformations() {
        logger.debug("@GetMapping(\"/me\")");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetail = (CustomUserDetails) authentication.getPrincipal();
    
        MeResponse meResponse = new MeResponse();
        meResponse.setName(userDetail.getName());
        meResponse.setEmail(userDetail.getEmail());
        meResponse.setCreated_at(userDetail.getCreatedAt().toString());
        meResponse.setUpdated_at(userDetail.getUpdatedAt().toString());
        logger.debug("Return response: {}", meResponse.toString());
        return ResponseEntity.ok(meResponse);
    }
}