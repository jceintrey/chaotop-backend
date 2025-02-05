package fr.jerem.chaotop_backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO for a message response.
 * <p>
 * Represents the response body returned after message creation.
 * </p>
 */
@Schema(description = "Response body returned after message creation")
@Data
@AllArgsConstructor
public class MessageResponse {
    @Schema(description = "Information message returned", example = "Message send with success")
    private String message;
}
