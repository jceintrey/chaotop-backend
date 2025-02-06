package fr.jerem.chaotop_backend.service;

import java.util.Optional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import fr.jerem.chaotop_backend.dto.LoginRequest;
import fr.jerem.chaotop_backend.dto.TokenResponse;
import fr.jerem.chaotop_backend.exception.AuthenticatedUserNotFound;

/**
 * Service responsible for handling user authentication and token generation.
 * <p>
 * This service interacts with the {@link AuthenticationManager} to authenticate
 * user credentials and with the {@link JwtFactory} to generate a JWT token upon
 * successful authentication.
 * It provides a method {@link #authenticate(LoginRequest)} that takes a
 * {@link LoginRequest}, authenticates the user, and returns a
 * {@link TokenResponse}
 * containing the generated JWT token.
 * </p>
 * 
 */
@Service
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final JwtFactory jwtFactory;

    public AuthenticationService(AuthenticationManager authenticationManager, JwtFactory jwtFactory) {
        this.authenticationManager = authenticationManager;
        this.jwtFactory = jwtFactory;
    }

    /**
     * Authenticates a user based on the provided login credentials.
     * <p>
     * This method uses {@link AuthenticationManager} to verify the user's identity
     * and get a JWT token upon successful authentication with the help of
     * {@link jwtFactory}.
     * </p>
     * 
     * @param {@link LoginRequest} the login request containing the user's email and
     *               password
     * @return a {@link TokenResponse} containing the generated JWT token
     * @throws Exception if authentication fails
     */
    public TokenResponse authenticate(LoginRequest request) throws Exception {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

            String token = jwtFactory.createToken(authentication);
            return new TokenResponse(token);

        } catch (AuthenticationException e) {
            throw new AuthenticationServiceException("Authentication failed", e);
        }
    }

    /**
     * Retrieves the email of the currently authenticated user.
     * <p>
     * This method accesses the {@link SecurityContextHolder} to obtain the
     * authentication details.
     * </p>
     * 
     * @return an {@link Optional} containing the authenticated user's email, or
     *         empty if no user is authenticated
     */
    public String getAuthenticatedUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        } else {
            throw new AuthenticatedUserNotFound("No authenticated user found in Security Context",
                    "AuhtenticationService.getAuthenticatedUserEmail");
        }
    }

}
