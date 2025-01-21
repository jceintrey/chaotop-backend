package fr.jerem.chaotop_backend.dto;

import lombok.Data;

/**
 * DTO that represents a login response
 * 
 * This class is used to return a structured json response with the jwt token
 * Example:
 * { jwt: ""}
 * <p>
 * Lombock is used to generate Getters/Setters
 * 
 */
@Data
public class TokenResponse {
    /**
     * Le jeton JWT généré pour l'authentification de l'utilisateur.
     */
    private String token;
}