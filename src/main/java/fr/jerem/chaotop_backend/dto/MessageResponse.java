package fr.jerem.chaotop_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO for a message response.
 * <p>
 * Represents the response body returned after message creation.
 * </p>
 */
@Data
@AllArgsConstructor
public class MessageResponse {
private String message;
}
