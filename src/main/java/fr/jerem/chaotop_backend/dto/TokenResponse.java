package fr.jerem.chaotop_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO that represents a Token response
 * 
 * This class is used to return the Json Web Token
 * 
 */
@Data
@AllArgsConstructor
public class TokenResponse {
    private String token;
}