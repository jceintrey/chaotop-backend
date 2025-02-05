package fr.jerem.chaotop_backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for a login request.
 * <p>
 * Represents the expected request body for user authentication.
 * </p>
 */
@Schema(description = "Request body for user login, containing credentials.")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    @Schema(description = "User's email", example = "user@example.com")
    private String email;
    @Schema(description = "User's password, must be secured.", example = "UsingUpperAndLowerAndSp€{I@/$CaseChars}")
    private String password;
}