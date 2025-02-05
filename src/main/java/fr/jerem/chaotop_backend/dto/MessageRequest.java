package fr.jerem.chaotop_backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
/**
 * DTO for a message request.
 * <p>
 * Represents the expected request body for message creation.
 * </p>
 */
@Schema(description = "Request body for message creation")
@Data
public class MessageRequest {

    @Schema(description = "User's message")
    private String message;
    @Schema(description = "User identifier of the message sender")
    @JsonProperty("user_id")
    private Long userId;
    @Schema(description = "Rental identifier")
    @JsonProperty("rental_id")
    private Long rentalId;
}
