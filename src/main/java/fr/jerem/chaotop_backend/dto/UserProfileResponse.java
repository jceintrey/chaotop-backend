package fr.jerem.chaotop_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for a user profile response.
 * <p>
 * Represents the response body returned after /me call.
 * </p>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileResponse {
    private Long id;
    private String name;
    private String email;
    private String created_at;
    private String updated_at;
}
