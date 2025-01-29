package fr.jerem.chaotop_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO that represents a login request posted on the login endpoint
 * 
 * This class is used to get the structure of the body parameters
 * <p>
 * Lombock is used to generate Getters/Setters
 * 
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    private String email;
    private String password;
}