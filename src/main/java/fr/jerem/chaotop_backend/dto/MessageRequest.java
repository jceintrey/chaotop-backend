package fr.jerem.chaotop_backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
/**
 * DTO for a message request.
 * <p>
 * Represents the expected request body for message creation.
 * </p>
 */
@Data
public class MessageRequest {
    private String message;
    @JsonProperty("user_id")
    private Long userId;
    @JsonProperty("rental_id")
    private Long rentalId;
}
