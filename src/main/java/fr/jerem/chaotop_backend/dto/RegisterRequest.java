package fr.jerem.chaotop_backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * DTO for a register request.
 * <p>
 * Represents the expected request body for user register.
 * </p>
 */
@Schema(description = "Request body for user register")
@Data
public class RegisterRequest {
    @Schema(description = "Name of the user to register", example = "John Doe")
    private String name;
    @Schema(description = "Email of the user to register", example = "jdoe@some.com")
    private String email;
    @Schema(description = "Clear Password of the user to register", example = "passwordshouldbesentoverasecurechanel")
    private String password;

}