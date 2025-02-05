package fr.jerem.chaotop_backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * DTO for a rental creation response.
 * <p>
 * Represents the response body returned after rental creation.
 * </p>
 */
@Schema(description = "Response body returned after rental creation")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RentalCreateResponse {
    @Schema(description = "Information message returned", example = "Rental created !")
    private String message;

}
