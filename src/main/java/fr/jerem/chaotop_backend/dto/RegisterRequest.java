package fr.jerem.chaotop_backend.dto;

import lombok.Data;

/**
 * DTO for a register request.
 * <p>
 * Represents the expected request body for user register.
 * </p>
 */
@Data
public class RegisterRequest {

    private String name;
    private String email;
    private String password;

}