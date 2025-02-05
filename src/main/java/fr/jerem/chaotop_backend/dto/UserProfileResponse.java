package fr.jerem.chaotop_backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for a user profile response.
 * <p>
 * Represents the response body returned after /me call.
 * </p>
 */
@Schema(description = "Response body containing the authenticated user informations.")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileResponse {
    @Schema(description = "User unique identifier")
    private Long id;
    @Schema(description = "User name")
    private String name;
    @Schema(description = "User email")
    private String email;
    @Schema(description = "Timestamp date of creation")
    private String created_at;
    @Schema(description = "Timestamp date of last update")
    private String updated_at;
}
